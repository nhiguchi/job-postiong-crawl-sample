import org.jsoup.Jsoup
import io.circe._, io.circe.parser._

object Main extends App {
  val detailUrl = "https://cookbiz.jp/job/job54024.html"
  val jobPostingScriptTag = Jsoup.connect(detailUrl).get().select("script[type=\"application/ld+json\"]")
  val jobPostingJson = parse(jobPostingScriptTag.html()) match { case Right(json) => json }

  // required values
  println("datePosted=[%s]".format(extract(jobPostingJson, "datePosted")))
  println("description=[%s]".format(extract(jobPostingJson, "description")))
  println("hiringOrganization=[%s]".format(extract(jobPostingJson, "hiringOrganization", "name")))
  println("jobLocation=[%s]".format(extract(jobPostingJson, "jobLocation", "addressRegion")))
  println("title=[%s]".format(extract(jobPostingJson, "title")))
  println("validThrough=[%s]".format(extract(jobPostingJson, "validThrough")))

  // recommended values
  println("applicantLocationRequirements=[%s]".format(extract(jobPostingJson, "applicantLocationRequirements")))
  println("baseSalary=[%s: %s~%s]".format(extract(jobPostingJson, "baseSalary", "unitText"), extract(jobPostingJson, "baseSalary", "minValue"), extract(jobPostingJson, "baseSalary", "maxValue")))
  println("employmentType=[%s]".format(extract(jobPostingJson, "employmentType")))
  println("identifier=[%s.%s]".format(extract(jobPostingJson, "identifier", "name"), extract(jobPostingJson, "identifier", "value")))
  println("jobLocationType=[%s]".format(extract(jobPostingJson, "jobLocationType")))

  def extract(json: Json, keys: String*) = {
    val result = keys.foldLeft(json)((j, k) => j.\\(k) match {
      case head :: _ => head
      case _ => Json.Null
    })
    if (result == Json.Null) "" else result.toString
  }
}

