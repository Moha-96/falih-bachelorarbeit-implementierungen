package de.hhu.rich.warenkorb.infrastructure.presentation.warenkorb;

import java.math.BigDecimal;
import java.util.List;

public record WarenkorbModel(String ID,
                             String kundeID,
                             List<WarenkorbzeileModel> warenkorbzeilenModel,
                             BigDecimal preis,
                             BigDecimal maxEinkaufswert) {
}
