package com.keywordr.util;

import com.keywordr.data.Company;
import com.keywordr.data.Job;
import com.keywordr.exception.KeywordrRuntimeException;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Keywordr {
    public static HashSet<Job> companiesJobListingCheck(List<Company> companies, HashSet<String> keywords, boolean useSelenium, int timeout) {
        HashSet<Job> jobs = new HashSet<>();
        String html;
        for(Company company : companies) {
            // Read company HTML
            try {
                if (useSelenium) {
                    html = HTMLReader.readHtmlWithSelenium(company.getURL(), timeout);
                } else {
                    html = HTMLReader.readHtmlWithJavaNet(company.getURL());
                }
            } catch (IOException e) {
                throw new KeywordrRuntimeException("Failed to read URL for the company " + company.getName());
            }

            // Get all <a> tags
            Elements linkTags = getAllTags(html, "a");

            // Check if each link tag contains any Job keyword
            for (Element linkTag : linkTags) {
                if(aTagContainsAnyKeyword(linkTag, keywords)) {
                    String href = linkTag.attr("href");
                    // If href is relative path, concat it with company URL
                    if (href.startsWith("/")) {
                        href = company.getURL().substring(0,indexOfNthChar(company.getURL(),'/',3)) + href;
                    }
                    // Skip anchor links
                    if (href.startsWith("#")) {
                        continue;
                    }
                    jobs.add(new Job(company, href, new HashSet<>()));
                }
            }
        }

        return jobs;
    }

    public static HashSet<Job> flagJobsWithKeywords(HashSet<Job> jobs, HashSet<String> keywords, boolean useSelenium, int timeout) {
        String html;
        for (Job job : jobs) {
            // Read Job HTML
            try {
                if (useSelenium) {
                    html = HTMLReader.readHtmlWithSelenium(job.getJobUrl(), timeout);
                } else {
                    html = HTMLReader.readHtmlWithJavaNet(job.getJobUrl());
                }
            } catch (IOException e) {
                throw new KeywordrRuntimeException("Failed to read URL for the job " + job);
            }

            // Get <body> tag html
            String body = getAllTags(html, "body").first().outerHtml();

            // Check if contains any of the tech keywords, if it does, flag the job with keyword
            for (String keyword : keywords) {
                if (containsWholeWordIgnoreCase(body,keyword)) {
                    job.getKeywords().add(keyword);
                }
            }

        }

        return jobs;
    }

    private static int indexOfNthChar(String str, char ch, int n) {
        int index = -1;
        while (n > 0) {
            index = str.indexOf(ch, index + 1);
            if (index == -1) {
                return -1;
            }
            n--;
        }
        return index;
    }

    public static boolean containsWholeWordIgnoreCase(String source, String word) {
        if (StringUtils.isBlank(source) || StringUtils.isBlank(word)) {
            return false;
        }

        // Use a regex to match whole words only, case-insensitive
        String regex = "\\b" + Pattern.quote(word) + "\\b";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(source);

        return matcher.find();
    }

    private static Elements getAllTags(String html, String tag) {
        return Jsoup.parse(html).select(tag);
    }
    private static boolean aTagContainsAnyKeyword(Element element, HashSet<String> keywords) {
        String outerHtml = element.outerHtml();
        return keywords.stream().anyMatch(keyword -> StringUtils.containsIgnoreCase(outerHtml, keyword));
    }

}
