name := "get-inline"

description := "Scala inline annotation that compiles against multiple Scala versions"

organization := "org.mdedetrich"

val scala212 = "2.12.18"
val scala213 = "2.13.11"
val scala3   = "3.3.1"

scalaVersion       := scala212
crossScalaVersions := Seq(scala212, scala213, scala3)

val isScala3 = Def.setting(scalaBinaryVersion.value == "3")

libraryDependencies ++= {
  if (isScala3.value)
    Seq.empty
  else
    Seq("org.scala-lang" % "scala-reflect" % scalaVersion.value % Provided)
}

scalacOptions ++= {
  CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2, n)) if n == 13 =>
      Seq(
        "-Ymacro-annotations"
      )
    case Some((2, n)) if n == 12 => Seq.empty
    case Some((3, _))            => Seq.empty
  }
}

libraryDependencies ++= {
  CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2, n)) if n == 13 => Seq.empty
    case Some((2, n)) if n == 12 =>
      Seq(compilerPlugin(("org.scalamacros" % "paradise" % "2.1.1").cross(CrossVersion.full)))
    case Some((3, _)) => Seq.empty
  }
}
