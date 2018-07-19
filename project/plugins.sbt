// The Lagom plugin
addSbtPlugin("com.lightbend.lagom" % "lagom-sbt-plugin" % "1.4.6")
// Override the Play version to get a fix for https://github.com/playframework/playframework/issues/8496
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.6.17")
// Needed for importing the project into Eclipse
addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "5.2.4")
