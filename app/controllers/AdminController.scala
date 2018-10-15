package controllers

import filters.WhenAdmin
import javax.inject.Inject
import play.api.Configuration
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}

class AdminController @Inject()(cc: ControllerComponents)(implicit conf : Configuration) extends AbstractController(cc) {

  def index() = WhenAdmin {
    Action { implicit request: Request[AnyContent] =>
      Ok(views.html.admin.index())
    }
  }


}
