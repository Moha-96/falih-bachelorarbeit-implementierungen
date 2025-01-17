package de.hhu.rich.warenkorb.infrastructure.persistence.warenkorb;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WarenkorbDAO {
    private static final WarenkorbDAO instance = new WarenkorbDAO();

    private final Map<UUID, WarenkorbDTO> repo = new HashMap<>();

    public static WarenkorbDAO getInstance() {
        return instance;
    }

    public WarenkorbDTO read(UUID id) {
        return this.repo.get(id);
    }

    public void update(WarenkorbDTO warenkorbDTO) {
        if(exists(warenkorbDTO.ID())) {
            this.repo.put(warenkorbDTO.ID(), warenkorbDTO);
        } else {
            throw new IllegalArgumentException("Update konnte nicht ausgeführt werden.");
        }
    }

    public void delete(UUID id) {
        this.repo.remove(id);
    }

    public boolean exists(UUID id) {
        return read(id) != null;
    }
}
