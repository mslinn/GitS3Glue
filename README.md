# Update AWS S3 content from Git post-receive hooks, via Tomcat running on Heroku #

## To Run ##

    git clone git@github.com:mslinn/HerokuTomcatAwsS3.git
    git remote add heroku git@heroku.com:sharp-cloud-8613.git # use your Heroku app name here


Define two environment variables to hold your AWS access key and your AWS secret key:

    heroku config:add accessKey=34poslkflskeflsekjfl
    heroku config:add secretKey=asdfoif3r3wfw3wgagawgawgawgw3taw3tatefef

Deploy the project to Heroku:

    git push heroku master

The web app should now be up and running on Heroku. Open it in your browser with:

    heroku open

## Use Case ##
 1. Using GitHub or BitBucket for source control.
 2. Storing content on AWS S3 (CDN).
 3. AWS S3 serves a static web site from the content.
 4. Commits to GitHub / BitBucket should cause changed files to be copied to AWS S3. Remember that S3 is
passive and cannot pull, and this Heroku app should not poll for changes. A post-receive hook or service
needs to run on GitHub / BitBucket to push to this Heroku app, which propagates changes to AWS S3.
 5. Additional processing might be done by this Heroku app for some or all commits.

## Git Post-Receive Service Hooks ##
A JSP is dedicated to receiving updates from each remote git service (GitHub or BitBucket).
The JSPs will perform the following when complete:

 1. Accept a POST in JSON format from the remote git service describing the commit.
 2. Verify the POST to be a result of a valid commit.
 3. Read a zip containing the committed files.
 4. Unpack the zip and push content files to AWS S3.

### GitHub WebHook URLs Hook ###
`fromGitHub.jsp` is not written yet.

The GitHub WebHook URLs(0) service is what we need.
Go to Admin / Service Hooks and pick the first entry, then enter the URL to POST to.

The service description says:
"We’ll hit these URLs with POST requests when you push to us, passing along information about the push.
More information can be found in the [Post-Receive Guide](http://help.github.com/post-receive-hooks/).
The Public IP addresses for these hooks are: 207.97.227.253, 50.57.128.197, 108.171.174.178."

FYI, GitHub's [service hooks](https://github.com/mslinn/HerokuTomcatAwsS3/admin/hooks) are open source, written in Ruby.
They include user-written hooks into the public list.
Docs are [here](https://github.com/github/github-services).

### BitBucket POST Service ###
[`fromBitBucket.jsp`](https://github.com/mslinn/GitS3Glue/blob/master/src/main/webapp/fromBitBucket.jsp) does the work.

Each time files are pushed to BitBucket, a POST can originate from the repo and can go a designated URL.
For the details on the services included with Bitbucket, check out [BitBucket services](https://confluence.atlassian.com/display/BITBUCKET/Managing+bitbucket+Services).
This Heroku app works with the [POST service](https://confluence.atlassian.com/display/BITBUCKET/Setting+Up+the+bitbucket+POST+Service).
