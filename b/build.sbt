scalaVersion := "2.10.4"

libraryDependencies += "org.example" %% "artifacta" % "1.0.0-SNAPSHOT"

libraryDependencies += "org.example" %% "artifacta" % "1.0.0-SNAPSHOT" withSources() classifier("tests") classifier("")

externalResolvers <<= (baseDirectory in ThisBuild) map { (buildDirectory) =>
	Seq(
		"demo" at ( buildDirectory / "demo-repo").toURI.toString,
		DefaultMavenRepository
	)
} 

