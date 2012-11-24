# Update AWS S3 content from Git post-receive hooks, via Tomcat running on Heroku #

## To Run ##

    git clone git@github.com:mslinn/HerokuTomcatAwsS3.git
    git remote add heroku git@heroku.com:gits3glue.git # use your Heroku app name here


Define two environment variables to hold your AWS access key and your AWS secret key:

    heroku config:add accessKey=34poslkflskeflsekjfl
    heroku config:add secretKey=asdfoif3r3wfw3wgagawgawgawgw3taw3tatefef

If you want to access a private repository on BitBucket, get OAuth credentials from support@BitBucket.com and
define two more environment variables:

    heroku config:add bbAccessKey=34poslkflskeflsekjfl
    heroku config:add bbSecretKey=asdfoif3r3wfw3wgagawgawgawgw3taw3tatefef

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
 3. Read each of the committed files and store into a temporary directory.
 4. Push content files to AWS S3.

 I had written a streaming copy utility from the Git repository to AWS S3 using NIO, but later discovered that AWS S3
 needs to know the file size prior to initiating a transfer.
 The only way I could discover the file size was to store each file locally :(
 Let's hope the temporary disk space accessible from Tomcat is big enough.
 2GB per file would be ideal.
 Not sure how many threads are available to the Heroku instance; I want to allocate as many threads as possible and
 transfer files in parallel.

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
Basic authentication doesn't work for some of direct file routes; an internal ticket has been opened.
I'll use OAuth for authenticating against private git repositories hosted on BitBucket.
