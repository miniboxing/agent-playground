import sbt._
import Keys._
import Process._
import sbtassembly.Plugin._
import AssemblyKeys._

object MiniboxingBuild extends Build {

  val scalaVer = "2.10.2-SNAPSHOT"

 val defaults = Defaults.defaultSettings ++ assemblySettings ++ Seq(
    scalaVersion := scalaVer,
    scalaBinaryVersion := "2.10",
    scalaSource in Compile <<= baseDirectory(_ / "src"),
    scalaSource in Test <<= baseDirectory(_ / "test"),
    resourceDirectory in Compile <<= baseDirectory(_ / "resources"),
    // scalacOptions ++= Seq("-optimize", "-Yinline-warnings"),
    compileOrder := CompileOrder.JavaThenScala,

    unmanagedSourceDirectories in Compile <<= (scalaSource in Compile)(Seq(_)),
    unmanagedSourceDirectories in Test <<= (scalaSource in Test)(Seq(_)),
    //http://stackoverflow.com/questions/10472840/how-to-attach-sources-to-sbt-managed-dependencies-in-scala-ide#answer-11683728
    com.typesafe.sbteclipse.plugin.EclipsePlugin.EclipseKeys.withSource := true,

    // this should work but it doesn't:
    // resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
    // so I replaced it with this, which works with both sbt 0.11 and 0.12 
    resolvers in ThisBuild ++= Seq(
      ScalaToolsSnapshots,
      "Sonatype Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
      "Sonatype Releases" at "http://oss.sonatype.org/content/repositories/releases"
    ),

    libraryDependencies ++= Seq(
      // "org.scala-lang" % "scala-library" % scalaVer,
      // "org.scala-lang" % "scala-reflect" % scalaVer,
      // "org.scala-lang" % "scala-compiler" % scalaVer,
      // "org.scala-lang" % "scala-partest" % scalaVer, 
      "org.scalacheck" %% "scalacheck" % "1.10.0" % "test",
      "com.novocode" % "junit-interface" % "0.10-M2" % "test"
    ),
    parallelExecution in Test := false,
    testOptions += Tests.Argument(TestFrameworks.JUnit, "-q", "-v")
  )

  val asm = Seq(
    libraryDependencies ++= Seq(
      "org.ow2.asm" % "asm" % "4.0",
      "org.ow2.asm" % "asm-tree" % "4.0",
      "org.ow2.asm" % "asm-util" % "4.0",
      "org.ow2.asm" % "asm-analysis" % "4.0"
    )
  )

  val scalaMeter = {
    val sMeter  = Seq("com.github.axel22" %% "scalameter" % "0.3")
    Seq(
      libraryDependencies ++= sMeter, 
      testFrameworks += new TestFramework("org.scalameter.ScalaMeterFramework")
    )
  }

  lazy val _agents     = Project(id = "agents-playground",             base = file(".")) aggregate (agent, benchmarks)
  lazy val agent       = Project(id = "agents-playground-agent",       base = file("components/runtime"), settings = defaults ++ asm)
  lazy val benchmarks  = Project(id = "agents-playground-benchmarks",  base = file("tests/benchmarks"),   settings = defaults ++ scalaMeter) dependsOn(agent)
}
