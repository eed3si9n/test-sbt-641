organization := "org.example"

name := "artifacta"

version := "1.0.0-SNAPSHOT"

scalaVersion := "2.10.4"

publishArtifact in (Test,packageBin) := true

publishTo <<= (baseDirectory in ThisBuild) { (buildDirectory) =>
	Some(Resolver.file("demo", buildDirectory / "demo-repo"))
} 
