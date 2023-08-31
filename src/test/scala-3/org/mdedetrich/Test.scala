package org.mdedetrich

import org.mdedetrich.getinline.getInline
import scala.annotation.experimental

@experimental object Test {
  @getInline final def myVal = 5
}
