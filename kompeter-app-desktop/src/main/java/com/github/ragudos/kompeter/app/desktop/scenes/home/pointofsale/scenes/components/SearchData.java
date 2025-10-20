package com.github.ragudos.kompeter.app.desktop.scenes.home.pointofsale.scenes.components;

public record SearchData(String searchText, String searchCategory, String searchBrand) {
    @Override
    public final boolean equals(Object arg0) {

        if (arg0 instanceof SearchData data) {
            return data.searchText().equals(searchText)
                    && data.searchCategory().equals(searchCategory)
                    && data.searchBrand().equals(searchBrand);
        }

        return false;
    }
}
