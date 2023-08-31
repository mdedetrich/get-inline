package org.mdedetrich.getinline

import scala.annotation.MacroAnnotation
import scala.quoted.Quotes
import scala.annotation.experimental

@experimental class getInline extends MacroAnnotation {
  override def transform(using quotes: Quotes)(
      tree: quotes.reflect.Definition
  ): List[quotes.reflect.Definition] = {
    import quotes.reflect._

    tree match {
      case e@ DefDef(name, params, tpt, rhs) =>
        val flagsWithInline = e.symbol.flags | Flags.Inline

        val methodWithInline = Symbol.newMethod(tree.symbol.owner,
                         e.name,
                         e.symbol.typeRef,
                         flagsWithInline,
                         e.symbol.privateWithin.map(_.termSymbol).getOrElse(Symbol.noSymbol)
        )

        // List(DefDef(methodWithInline, _ => e.rhs))

        

        List(DefDef.copy(e)(name, params, tpt, rhs))
      case e: ValDef =>
        val flagsWithInline = e.symbol.flags | Flags.Inline

        val valWithInline = Symbol.newVal(tree.symbol.owner,
                      e.name,
                      e.symbol.typeRef,
                      flagsWithInline,
                      e.symbol.privateWithin.map(_.termSymbol).getOrElse(Symbol.noSymbol)
        )

        List(ValDef(valWithInline, e.rhs))
    }
  }

}
