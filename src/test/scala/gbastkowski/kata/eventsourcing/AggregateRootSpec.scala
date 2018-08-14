package gbastkowski.kata.eventsourcing

import org.scalatest._

class AggregateRootSpec extends FreeSpec with Matchers {

  "An empty invoice" - {
    "can be created" in {
      val invoice = Invoice()
      Invoice.loadFromHistory(Seq(
        InvoiceCreated(1)))
    }
  }

}
