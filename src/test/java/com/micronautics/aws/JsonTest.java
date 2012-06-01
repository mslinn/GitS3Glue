package com.micronautics.aws;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JsonTest {
    String payload = "{\n" +
            "      \"repository\": {\n" +
            "        \"website\": \"http://www.asdf.com/\",\n" +
            "        \"fork\": false,\n" +
            "        \"name\": \"asdf.www\",\n" +
            "        \"scm\": \"git\",\n" +
            "        \"absolute_url\": \"/mslinn/asdf.www/\",\n" +
            "        \"owner\": \"mslinn\",\n" +
            "        \"slug\": \"asdf.www\",\n" +
            "        \"is_private\": true\n" +
            "      },\n" +
            "      \"commits\": [\n" +
            "        {\"node\": \"b51cd557430b\",\n" +
            "         \"files\": [{\"type\": \"added\", \"file\": \"store/robots.txt\"}],\n" +
            "         \"branch\": \"master\",\n" +
            "         \"utctimestamp\": \"2012-05-31 05:01:58+00:00\",\n" +
            "         \"author\": \"mslinn\",\n" +
            "         \"timestamp\": \"2012-05-31 07:01:58\",\n" +
            "         \"raw_node\": \"babcdef7430bae0d3c729744ac5572c9dbaabe48\",\n" +
            "         \"parents\": [\"bbabdef40bce\"],\n" +
            "         \"raw_author\": \"Mike Slinn \",\n" +
            "         \"message\": \"asdf\\n\",\n" +
            "         \"size\": -1,\n" +
            "         \"revision\": null}\n" +
            "      ],\n" +
            "      \"canon_url\": \"https://bitbucket.org\",\n" +
            "      \"user\": \"mslinn\"\n" +
            "}";

    @BeforeClass
    public static void setUpClass() { }

    @AfterClass
    public static void tearDownClass() { }

    @Test
    public void parse() {
        //System.out.println(payload);
        Commit commit = JSON.parse(payload);
        String[] keys = commit.files.keySet().toArray(new String[commit.files.keySet().size()]);
        assertEquals(1, keys.length);
        assertEquals("asdf.www", commit.name);
        assertEquals("store/robots.txt", keys[0]);
        assertEquals("added", commit.files.get(keys[0]));
    }
}