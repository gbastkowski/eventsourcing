package gbastkowski.kata.eventsourcing

import org.scalatest._

class AggregateRootSpec extends FreeSpec with Matchers {

  "An empty invoice" - {
    "can be created" in {
      val invoice = Invoice.create(1)

      Invoice.loadFromHistory(Seq(
        InvoiceEvent.Created(1)))
    }
  }

}
