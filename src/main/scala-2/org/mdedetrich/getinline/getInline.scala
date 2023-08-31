package org.mdedetrich.getinline

import scala.annotation.{StaticAnnotation, compileTimeOnly}
import scala.language.experimental.macros
import scala.reflect.macros.blackbox.Context

@compileTimeOnly("enable macros to expand macro annotations")
class getInline extends StaticAnnotation {
  def macroTransform(annottees: Any*): Any = macro getInlineMacro.impl
}

private[getinline] object getInlineMacro {

  def impl(c: Context)(annottees: c.Expr[Any]*): c.Expr[Any] = {
    import c.universe._

    val warning = "@getInline must be used on a method or val"

    annottees.headOption match {
      case Some(single) =>
        single.tree match {
          case e: ValOrDefDef =>
            val annotation = tq"_root_.scala.inline"

            val annotationsWithInline = e.mods.mapAnnotations { list =>
              if (list.exists(_.exists(x => c.typecheck(x, silent = true).tpe =:= typeOf[inline]))) {
                c.warning(c.enclosingPosition, "Skipping since definition already has @inline annotation")
                list
              } else
                q"new ($annotation)" +: list
            }

            e match {
              case defDef: DefDef =>
                c.Expr(
                  DefDef(annotationsWithInline, defDef.name, defDef.tparams, defDef.vparamss, defDef.tpt, defDef.rhs)
                )
              case valDef: ValDef =>
                c.Expr(ValDef(annotationsWithInline, valDef.name, valDef.tpt, valDef.rhs))
            }
          case _ => c.abort(c.enclosingPosition, warning)
        }

      case None => c.abort(c.enclosingPosition, warning)
    }
  }
}
