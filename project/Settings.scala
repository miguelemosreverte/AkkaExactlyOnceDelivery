import sbt._
import sbt.Keys._
import com.typesafe.sbt.SbtScalariform._
import scalariform.formatter.preferences._

object Settings extends Dependencies with CommonScalac {
    val modulesSettings = Seq(
      scalacOptions ++= scalacSettings,
      scalaVersion := scalaVersionUsed,
      resolvers ++= commonResolvers,
      libraryDependencies ++= mainDeps,
      libraryDependencies ++= testDeps map (_ % Test)
    )
}