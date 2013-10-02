import anorm.NotAssigned
import models.{Project, WorkItem}

import org.joda.time.DateTime
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers


import play.api.test._
import play.api.test.Helpers._

class WorkItemSpec extends FlatSpec with ShouldMatchers {

  "A WorkItem" should "be savable" in {
    running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      val projectId = Project.save(Project(NotAssigned, "testProject", Some("1234-xc")))
      val id = WorkItem.save(WorkItem(NotAssigned, projectId.get, new DateTime(), new DateTime(), 30, "description"))
      id should not equal (None)
    }
  }

  "WorkItems" should "be retrievable by projectId" in {
    running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      val projectId = Project.save(Project(NotAssigned, "testProject", Some("1234-xc")))
      val projectId2 = Project.save(Project(NotAssigned, "testProject2", Some("666-cv")))
      val id = WorkItem.save(WorkItem(NotAssigned, projectId.get, new DateTime(), new DateTime(), 30, "description"))
      val id2 = WorkItem.save(WorkItem(NotAssigned, projectId2.get, new DateTime(), new DateTime(), 30, "task1"))
      val workItems = WorkItem.getByProject(projectId.get)
      workItems.size should equal (1)
    }
  }

  "WorkItems" should "be retrieved grouped by Project" in {
    running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
      val projectId = Project.save(Project(NotAssigned, "testProject", Some("1234-xc")))
      val projectId2 = Project.save(Project(NotAssigned, "testProject2", Some("666-cv")))
      val id = WorkItem.save(WorkItem(NotAssigned, projectId.get, new DateTime(), new DateTime(), 30, "description"))
      val id2 = WorkItem.save(WorkItem(NotAssigned, projectId2.get, new DateTime(), new DateTime(), 30, "task1"))
      val id3 = WorkItem.save(WorkItem(NotAssigned, projectId2.get, new DateTime(), new DateTime(), 10, "task2"))
      val groupedMap = WorkItem.groupedByProjectId
      groupedMap.size should equal (2)
      (groupedMap get projectId.get).get.size should equal (1)
      (groupedMap get projectId2.get).get.size should equal (2)

    }
  }

}
