(ns de.hhu.anemic.warenkorb.domain.model.artikel.ArtikelRepository)

(defprotocol ArtikelRepository
  (findeMit [this artikel-id])
  (speichere [this artikel])
  (entferne [this artikel-id]))

