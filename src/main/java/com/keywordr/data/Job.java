package com.keywordr.data;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.HashSet;

@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class Job {
    private Company company;
    private String jobUrl;
    private HashSet<String> keywords;
}
