package points

case class SurveyAreaCharacteristics[T](surveyArea: Area[T], catchmentAreas: Seq[CatchmentArea])
