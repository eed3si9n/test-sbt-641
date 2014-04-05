import sbt._
import Keys._

object Builds extends sbt.Build {
  def localCache = 
    ivyPaths <<= (baseDirectory, baseDirectory in ThisBuild) { (base, buildBase) =>
      new IvyPaths(base, Some(buildBase / "ivy" / "cache"))
    }

  val b = Project("a", file("a")).settings(localCache)

  val a = Project("b", file("b")).settings(localCache)
}
