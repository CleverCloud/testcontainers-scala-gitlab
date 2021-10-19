package com.clevercloud.testcontainers.scala

import com.clevercloud.testcontainers.gitlab.{GitlabContainer => JavaGitlabContainer}
import com.dimafeng.testcontainers._

/** Create a Gitlab container
 *
 * @param tag          The version tag for the docker image
 */
case class GitlabContainer(
  tag: String
) extends SingleContainer[JavaGitlabContainer] {

  override val container: JavaGitlabContainer = new JavaGitlabContainer(tag)

  def httpHost: String = container.getHTTPHost

  def httpPort: Int = container.getHTTPPort()

  def httpHostAddress: String = container.getHTTPHostAddress

  def protocol: String = container.getProtocol

  def gitlabRootPassword: String = container.getGitlabRootPassword
}

object GitlabContainer {
  val defaultTag = "14.3.3-ce.0"

  case class Def(
    tag: String,
  ) extends ContainerDef {
    override type Container = GitlabContainer

    override def createContainer(): GitlabContainer = {
      GitlabContainer(tag)
    }
  }
}
