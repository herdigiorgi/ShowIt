package com.hdigiorgi.showPhoto.model
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test._
import play.api.test.Helpers._
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test._
import play.api.test.Helpers._
import org.scalatest.FunSuite
import com.hdigiorgi.showPhoto.model.db._
import javax.inject._
import play.api.Configuration

class ModelTest extends FunSuite with GuiceOneAppPerTest with Injecting with test.UseTestConfig {
  test("Smoke license persistence") {
    val db = new PersistentLicense(fakeApplication().configuration)
    db.init()
    assert(Set.empty.size == 0)
  }
}
