package org.mdedetrich

import org.mdedetrich.getinline.getInline

object Test {

  @getInline final def myMethod() = 5

  @getInline final val myVal = 5

}
