# Get Inline

*Scala compiler, get in line dammit!*

Get Inline is a simple cross Scala 2/Scala 3 `@getInline` Macro Annotation that ensures that the superseding
`val`/`def` gets inlined. This is achieved by the macro transforming the macro to use the `@inline` annotation for
Scala 2 and the `inline` keyword for Scala 3.
