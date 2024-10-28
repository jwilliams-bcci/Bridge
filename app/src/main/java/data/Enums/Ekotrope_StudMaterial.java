package data.Enums;

import androidx.annotation.NonNull;

public enum Ekotrope_StudMaterial {
    WOOD("WOOD"),
    STEEL("STEEL"),
    CONCRETE("CONCRETE");

    public String type;

    Ekotrope_StudMaterial(String type) {
        this.type = type;
    }

    @NonNull
    @Override
    public String toString() { return type; }
}

