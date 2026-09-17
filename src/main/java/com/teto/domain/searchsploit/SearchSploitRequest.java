package com.teto.domain.searchsploit;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class SearchSploitRequest {
    private List<String> keyTerms = new ArrayList<>();
    private List<String> excludes = new ArrayList<>();
    private boolean caseSensitive = false;
    private boolean requireAllItemsMatch = false;
    private boolean exactMatch = false;

}
