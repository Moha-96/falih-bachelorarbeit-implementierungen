(ns de.hhu.rich.warenkorb.domain.model.warenkorb.WarenkorbRepository)

(defprotocol WarenkorbRepository
  (findeMit [this warenkorb-id])
  (speichere [this warenkorb])
  (entferne [this warenkorb-id]))