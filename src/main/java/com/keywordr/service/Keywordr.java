package com.keywordr.service;

import com.keywordr.data.Company;
import com.keywordr.data.Job;
import com.keywordr.exception.KeywordrRuntimeException;
import com.keywordr.provider.LoggerProvider;
import com.keywordr.io.reader.HTMLReader;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Keywordr {
    private static final Logger logger;

    static {
        try {
            logger = LoggerProvider.newInstance(Keywordr.class);
        } catch (IOException e) {
            throw new KeywordrRuntimeException("Failed to initialize Keywordr with message: " + e.getMessage());
        }
    }
    public static Set<Job> companiesJobListingCheck(List<Company> companies, Set<String> keywords, boolean useSelenium, int timeout) {
        Set<Job> jobs = new HashSet<>();
        String html;
        logger.debug("Companies listing check started.");

        for(Company company : companies) {
            // Read company HTML
            logger.debug("Reading HTML for company: '" + company + "'.");
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
            logger.debug("Retrieving all link tags in the HTML.");
            Elements linkTags = getAllTags(html, "a");
            logger.debug("Total of " + linkTags.size() + " link tags found.");

            // Check if each link tag contains any Job keyword
            for (Element linkTag : linkTags) {
                logger.debug("Checking if link tag '" + linkTag.outerHtml() + "' contains any Job keyword.");
                if(aTagContainsAnyKeyword(linkTag, keywords)) {
                    String href = linkTag.attr("href");
                    // If href is relative path, concat it with company URL
                    if (href.startsWith("/")) {
                        href = company.getURL().substring(0,indexOfNthChar(company.getURL(),'/',3)) + href;
                    }
                    // Skip anchor links
                    if (href.startsWith("#")) {
                        logger.debug("Oops! It is an anchor link. Skipping...");
                        continue;
                    }
                    logger.debug("Found a job link adding it to a Job set.");
                    jobs.add(new Job(company.getName(), company.getLocation(), href, new ArrayList<>()));
                }
            }
        }

        return jobs;
    }

    public static Set<Job> flagJobsWithKeywords(Set<Job> jobs, HashSet<String> keywords, boolean useSelenium, int timeout) {
        String html;
        logger.debug("Flag jobs with keywords started.");
        for (Job job : jobs) {
            // Read Job HTML
            logger.debug("Reading HTML for job '" + job.getJobUrl() + "'.");
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
                    logger.debug("Keyword '" + keyword + "' found for a job '" + job.getJobUrl() + "'.");
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

    private static boolean containsWholeWordIgnoreCase(String source, String word) {
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
    private static boolean aTagContainsAnyKeyword(Element element, Set<String> keywords) {
        String outerHtml = element.outerHtml();
        return keywords.stream().anyMatch(keyword -> StringUtils.containsIgnoreCase(outerHtml, keyword));
    }

}
