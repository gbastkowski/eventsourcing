package gbastkowski.kata.eventsourcing

import org.scalatest._

class AggregateFactorySpec extends FreeSpec with Matchers {

  "An empty invoice can be loaded" in {
      val invoice = Invoice.
        loadFromHistory(Seq(
          InvoiceEvent.Created(1)))
      invoice.id shouldBe 1
  }

  "An invoice with a lineItem can be loaded" in {
    import InvoiceEvent._
    val invoice = Invoice.loadFromHistory(Seq(
        Created(1),
        ItemAdded(InvoiceItem(1, "Item", 1))))
    invoice.items should have size (1)
  }

}
