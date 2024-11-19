package com.keywordr.data;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class Job implements Serializable {
    private String companyName;
    private String location;
    private String jobUrl;
    private List<String> keywords;
}
