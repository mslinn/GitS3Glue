package com.micronautics.aws;

import java.util.TreeMap;

public class Commit {
    /** Sorted map of file path to action, where action is one of: "added", "deleted".
     * map is sorted by key (file path) */
    public TreeMap<String, String> files = new TreeMap<String, String>();

    /** Name of repository, must be same as name of AWS S3 bucket */
    public String name = "";
}
