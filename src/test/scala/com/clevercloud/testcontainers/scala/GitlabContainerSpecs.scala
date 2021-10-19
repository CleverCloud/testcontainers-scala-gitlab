package com.clevercloud.testcontainers.scala

import akka.http.scaladsl.model.{HttpEntity, HttpMethods}
import akka.actor.testkit.typed.scaladsl.ActorTestKit
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.unmarshalling.Unmarshal
import io.moia.scalaHttpClient._
import org.scalatest.concurrent.Eventually
import org.scalatest.matchers.should.Matchers
import org.scalatest.time.{Milliseconds, Seconds, Span}
import org.scalatest.wordspec.AnyWordSpec
import com.dimafeng.testcontainers.ForAllTestContainer

import java.time.Clock
import scala.concurrent.{Await, Future}
import scala.concurrent.duration.{Deadline, DurationInt}


class GitlabContainerSpecs extends AnyWordSpec
                                   with Matchers
                                   with Eventually
                                   with ForAllTestContainer {
  private val GitlabUsersAPI = "/api/v4/users"
  private val GitlabVersion = "14.3.3-ce.0"

  val container = GitlabContainer(GitlabVersion)

  private val testKit = ActorTestKit()

  implicit val system = testKit.system
  implicit val ec = system.executionContext

  "gitlab testcontainer" should {
    "get the users list" in {
      implicit val patienceConfig: PatienceConfig = PatienceConfig(timeout = Span(5, Seconds), interval = Span(50, Milliseconds))

      Await.result(gitlabRequest(container, GitlabUsersAPI, Some(container.gitlabRootPassword)), 10.seconds) match {
        case HttpClientSuccess(content) =>
          content.status.isSuccess() shouldBe (true)
        case DomainError(content) =>
          Unmarshal(content).to[String] shouldBe (false)
        case _                    =>
          "error" shouldBe (false)
      }
    }
  }

  def gitlabRequest(
    container: GitlabContainer,
    path: String,
    auth: Option[String],
  ): Future[HttpClientResponse] = {
    val httpClient = new HttpClient(
      config = HttpClientConfig("http", container.httpHost, container.httpPort),
      name = "TestClient",
      httpMetrics = HttpMetrics.none,
      retryConfig = RetryConfig.default,
      clock = Clock.systemUTC(),
      awsRequestSigner = None
    )

    httpClient.request(
      method = HttpMethods.GET,
      path = path,
      entity = HttpEntity.Empty,
      headers = Seq(auth.map(auth => RawHeader("Authorization", "Bearer " + auth))).flatten,
      deadline = Deadline.now + 10.seconds
    )
  }
}
