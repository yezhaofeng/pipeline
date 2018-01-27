package com.jlu.github.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.codehaus.jettison.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.jlu.github.model.GitHubCommit;
import com.jlu.github.model.Module;
import com.jlu.github.service.IModuleService;

import net.sf.json.JSONObject;

/**
 * Created by langshiquan on 18/1/27.
 */
@RunWith(MockitoJUnitRunner.class)
public class GitHubHookServiceImplTest {
    private String hookMessage = "{\n"
            + "  \"ref\": \"refs/heads/master\",\n"
            + "  \"before\": \"f6471514996d74cfb125011be4cc18599244cceb\",\n"
            + "  \"after\": \"9f2bae75366c3f3023dc040f3253496eb3190dbf\",\n"
            + "  \"created\": false,\n"
            + "  \"deleted\": false,\n"
            + "  \"forced\": false,\n"
            + "  \"base_ref\": null,\n"
            + "  \"compare\": \"https://github.com/z521598/pipeline_v2/compare/f6471514996d...9f2bae75366c\",\n"
            + "  \"commits\": [\n"
            + "    {\n"
            + "      \"id\": \"9f2bae75366c3f3023dc040f3253496eb3190dbf\",\n"
            + "      \"tree_id\": \"61d00fc79fc553c327d2fa06051d487eb93a0664\",\n"
            + "      \"distinct\": true,\n"
            + "      \"message\": \"hookurl\",\n"
            + "      \"timestamp\": \"2018-01-27T14:29:08+08:00\",\n"
            + "      \"url\": \"https://github"
            + ".com/z521598/pipeline_v2/commit/9f2bae75366c3f3023dc040f3253496eb3190dbf\",\n"
            + "      \"author\": {\n"
            + "        \"name\": \"langshiquan\",\n"
            + "        \"email\": \"langshiquan@baidu.com\"\n"
            + "      },\n"
            + "      \"committer\": {\n"
            + "        \"name\": \"langshiquan\",\n"
            + "        \"email\": \"langshiquan@baidu.com\"\n"
            + "      },\n"
            + "      \"added\": [\n"
            + "\n"
            + "      ],\n"
            + "      \"removed\": [\n"
            + "\n"
            + "      ],\n"
            + "      \"modified\": [\n"
            + "        \"src/main/resources/application.properties\"\n"
            + "      ]\n"
            + "    }\n"
            + "  ],\n"
            + "  \"head_commit\": {\n"
            + "    \"id\": \"9f2bae75366c3f3023dc040f3253496eb3190dbf\",\n"
            + "    \"tree_id\": \"61d00fc79fc553c327d2fa06051d487eb93a0664\",\n"
            + "    \"distinct\": true,\n"
            + "    \"message\": \"hookurl\",\n"
            + "    \"timestamp\": \"2018-01-27T14:29:08+08:00\",\n"
            + "    \"url\": \"https://github.com/z521598/pipeline_v2/commit/9f2bae75366c3f3023dc040f3253496eb3190dbf"
            + "\",\n"
            + "    \"author\": {\n"
            + "      \"name\": \"langshiquan\",\n"
            + "      \"email\": \"langshiquan@baidu.com\"\n"
            + "    },\n"
            + "    \"committer\": {\n"
            + "      \"name\": \"langshiquan\",\n"
            + "      \"email\": \"langshiquan@baidu.com\"\n"
            + "    },\n"
            + "    \"added\": [\n"
            + "\n"
            + "    ],\n"
            + "    \"removed\": [\n"
            + "\n"
            + "    ],\n"
            + "    \"modified\": [\n"
            + "      \"src/main/resources/application.properties\"\n"
            + "    ]\n"
            + "  },\n"
            + "  \"repository\": {\n"
            + "    \"id\": 117507983,\n"
            + "    \"name\": \"pipeline_v2\",\n"
            + "    \"full_name\": \"z521598/pipeline_v2\",\n"
            + "    \"owner\": {\n"
            + "      \"name\": \"z521598\",\n"
            + "      \"email\": \"576506402@qq.com\",\n"
            + "      \"login\": \"z521598\",\n"
            + "      \"id\": 20750824,\n"
            + "      \"avatar_url\": \"https://avatars1.githubusercontent.com/u/20750824?v=4\",\n"
            + "      \"gravatar_id\": \"\",\n"
            + "      \"url\": \"https://api.github.com/users/z521598\",\n"
            + "      \"html_url\": \"https://github.com/z521598\",\n"
            + "      \"followers_url\": \"https://api.github.com/users/z521598/followers\",\n"
            + "      \"following_url\": \"https://api.github.com/users/z521598/following{/other_user}\",\n"
            + "      \"gists_url\": \"https://api.github.com/users/z521598/gists{/gist_id}\",\n"
            + "      \"starred_url\": \"https://api.github.com/users/z521598/starred{/owner}{/repo}\",\n"
            + "      \"subscriptions_url\": \"https://api.github.com/users/z521598/subscriptions\",\n"
            + "      \"organizations_url\": \"https://api.github.com/users/z521598/orgs\",\n"
            + "      \"repos_url\": \"https://api.github.com/users/z521598/repos\",\n"
            + "      \"events_url\": \"https://api.github.com/users/z521598/events{/privacy}\",\n"
            + "      \"received_events_url\": \"https://api.github.com/users/z521598/received_events\",\n"
            + "      \"type\": \"User\",\n"
            + "      \"site_admin\": false\n"
            + "    },\n"
            + "    \"private\": false,\n"
            + "    \"html_url\": \"https://github.com/z521598/pipeline_v2\",\n"
            + "    \"description\": null,\n"
            + "    \"fork\": false,\n"
            + "    \"url\": \"https://github.com/z521598/pipeline_v2\",\n"
            + "    \"forks_url\": \"https://api.github.com/repos/z521598/pipeline_v2/forks\",\n"
            + "    \"keys_url\": \"https://api.github.com/repos/z521598/pipeline_v2/keys{/key_id}\",\n"
            + "    \"collaborators_url\": \"https://api.github"
            + ".com/repos/z521598/pipeline_v2/collaborators{/collaborator}\",\n"
            + "    \"teams_url\": \"https://api.github.com/repos/z521598/pipeline_v2/teams\",\n"
            + "    \"hooks_url\": \"https://api.github.com/repos/z521598/pipeline_v2/hooks\",\n"
            + "    \"issue_events_url\": \"https://api.github.com/repos/z521598/pipeline_v2/issues/events{/number}\",\n"
            + "    \"events_url\": \"https://api.github.com/repos/z521598/pipeline_v2/events\",\n"
            + "    \"assignees_url\": \"https://api.github.com/repos/z521598/pipeline_v2/assignees{/user}\",\n"
            + "    \"branches_url\": \"https://api.github.com/repos/z521598/pipeline_v2/branches{/branch}\",\n"
            + "    \"tags_url\": \"https://api.github.com/repos/z521598/pipeline_v2/tags\",\n"
            + "    \"blobs_url\": \"https://api.github.com/repos/z521598/pipeline_v2/git/blobs{/sha}\",\n"
            + "    \"git_tags_url\": \"https://api.github.com/repos/z521598/pipeline_v2/git/tags{/sha}\",\n"
            + "    \"git_refs_url\": \"https://api.github.com/repos/z521598/pipeline_v2/git/refs{/sha}\",\n"
            + "    \"trees_url\": \"https://api.github.com/repos/z521598/pipeline_v2/git/trees{/sha}\",\n"
            + "    \"statuses_url\": \"https://api.github.com/repos/z521598/pipeline_v2/statuses/{sha}\",\n"
            + "    \"languages_url\": \"https://api.github.com/repos/z521598/pipeline_v2/languages\",\n"
            + "    \"stargazers_url\": \"https://api.github.com/repos/z521598/pipeline_v2/stargazers\",\n"
            + "    \"contributors_url\": \"https://api.github.com/repos/z521598/pipeline_v2/contributors\",\n"
            + "    \"subscribers_url\": \"https://api.github.com/repos/z521598/pipeline_v2/subscribers\",\n"
            + "    \"subscription_url\": \"https://api.github.com/repos/z521598/pipeline_v2/subscription\",\n"
            + "    \"commits_url\": \"https://api.github.com/repos/z521598/pipeline_v2/commits{/sha}\",\n"
            + "    \"git_commits_url\": \"https://api.github.com/repos/z521598/pipeline_v2/git/commits{/sha}\",\n"
            + "    \"comments_url\": \"https://api.github.com/repos/z521598/pipeline_v2/comments{/number}\",\n"
            + "    \"issue_comment_url\": \"https://api.github"
            + ".com/repos/z521598/pipeline_v2/issues/comments{/number}\",\n"
            + "    \"contents_url\": \"https://api.github.com/repos/z521598/pipeline_v2/contents/{+path}\",\n"
            + "    \"compare_url\": \"https://api.github.com/repos/z521598/pipeline_v2/compare/{base}...{head}\",\n"
            + "    \"merges_url\": \"https://api.github.com/repos/z521598/pipeline_v2/merges\",\n"
            + "    \"archive_url\": \"https://api.github.com/repos/z521598/pipeline_v2/{archive_format}{/ref}\",\n"
            + "    \"downloads_url\": \"https://api.github.com/repos/z521598/pipeline_v2/downloads\",\n"
            + "    \"issues_url\": \"https://api.github.com/repos/z521598/pipeline_v2/issues{/number}\",\n"
            + "    \"pulls_url\": \"https://api.github.com/repos/z521598/pipeline_v2/pulls{/number}\",\n"
            + "    \"milestones_url\": \"https://api.github.com/repos/z521598/pipeline_v2/milestones{/number}\",\n"
            + "    \"notifications_url\": \"https://api.github.com/repos/z521598/pipeline_v2/notifications{?since,"
            + "all,participating}\",\n"
            + "    \"labels_url\": \"https://api.github.com/repos/z521598/pipeline_v2/labels{/name}\",\n"
            + "    \"releases_url\": \"https://api.github.com/repos/z521598/pipeline_v2/releases{/id}\",\n"
            + "    \"deployments_url\": \"https://api.github.com/repos/z521598/pipeline_v2/deployments\",\n"
            + "    \"created_at\": 1515998918,\n"
            + "    \"updated_at\": \"2018-01-15T06:50:05Z\",\n"
            + "    \"pushed_at\": 1517034554,\n"
            + "    \"git_url\": \"git://github.com/z521598/pipeline_v2.git\",\n"
            + "    \"ssh_url\": \"git@github.com:z521598/pipeline_v2.git\",\n"
            + "    \"clone_url\": \"https://github.com/z521598/pipeline_v2.git\",\n"
            + "    \"svn_url\": \"https://github.com/z521598/pipeline_v2\",\n"
            + "    \"homepage\": null,\n"
            + "    \"size\": 449,\n"
            + "    \"stargazers_count\": 0,\n"
            + "    \"watchers_count\": 0,\n"
            + "    \"language\": \"Java\",\n"
            + "    \"has_issues\": true,\n"
            + "    \"has_projects\": true,\n"
            + "    \"has_downloads\": true,\n"
            + "    \"has_wiki\": true,\n"
            + "    \"has_pages\": false,\n"
            + "    \"forks_count\": 0,\n"
            + "    \"mirror_url\": null,\n"
            + "    \"archived\": false,\n"
            + "    \"open_issues_count\": 0,\n"
            + "    \"license\": null,\n"
            + "    \"forks\": 0,\n"
            + "    \"open_issues\": 0,\n"
            + "    \"watchers\": 0,\n"
            + "    \"default_branch\": \"master\",\n"
            + "    \"stargazers\": 0,\n"
            + "    \"master_branch\": \"master\"\n"
            + "  },\n"
            + "  \"pusher\": {\n"
            + "    \"name\": \"z521598\",\n"
            + "    \"email\": \"576506402@qq.com\"\n"
            + "  },\n"
            + "  \"sender\": {\n"
            + "    \"login\": \"z521598\",\n"
            + "    \"id\": 20750824,\n"
            + "    \"avatar_url\": \"https://avatars1.githubusercontent.com/u/20750824?v=4\",\n"
            + "    \"gravatar_id\": \"\",\n"
            + "    \"url\": \"https://api.github.com/users/z521598\",\n"
            + "    \"html_url\": \"https://github.com/z521598\",\n"
            + "    \"followers_url\": \"https://api.github.com/users/z521598/followers\",\n"
            + "    \"following_url\": \"https://api.github.com/users/z521598/following{/other_user}\",\n"
            + "    \"gists_url\": \"https://api.github.com/users/z521598/gists{/gist_id}\",\n"
            + "    \"starred_url\": \"https://api.github.com/users/z521598/starred{/owner}{/repo}\",\n"
            + "    \"subscriptions_url\": \"https://api.github.com/users/z521598/subscriptions\",\n"
            + "    \"organizations_url\": \"https://api.github.com/users/z521598/orgs\",\n"
            + "    \"repos_url\": \"https://api.github.com/users/z521598/repos\",\n"
            + "    \"events_url\": \"https://api.github.com/users/z521598/events{/privacy}\",\n"
            + "    \"received_events_url\": \"https://api.github.com/users/z521598/received_events\",\n"
            + "    \"type\": \"User\",\n"
            + "    \"site_admin\": false\n"
            + "  }\n"
            + "}";

    @InjectMocks
    private GitHubHookServiceImpl gitHubHookService = new GitHubHookServiceImpl();

    @Mock
    private IModuleService moduleService;

    @Test
    public void testDealHookMessage()
            throws JSONException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Mockito.when(moduleService.getModuleByUserAndModule(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(new Module());
        Method getCommitByHookMethod = gitHubHookService.getClass().getDeclaredMethod("getCommitByHook", JSONObject
                .class);
        getCommitByHookMethod.setAccessible(true);

        JSONObject hookMessageObject = JSONObject.fromObject(hookMessage.toString());
        GitHubCommit gitHubCommit = (GitHubCommit) getCommitByHookMethod.invoke(gitHubHookService, hookMessageObject);
        System.out.println(gitHubCommit);

    }

}