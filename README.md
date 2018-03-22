淇敼浼犻�渚濊禆妫�煡,浠呮鏌ユ槗鏋佷粯渚濊禆鏄惁鏈変紶閫掍緷璧栭棶棰�`org.apache.maven.plugins.enforcer.RequireUpperBoundDeps$RequireUpperBoundDepsVisitor#containsConflicts`
鍔犲叆濡備笅浠ｇ爜:

	String key=  resolvedPair.constructKey();
	if(key!=null&&!(key.startsWith("com.global")||key.startsWith("com.global"))){
  		return false;
	}


Deploying web site
-------------------
You can use the deploySite(.sh|.bat) script
Without any profile, the site will be deployed to http://maven.apache.org/enforcer-archives/enforcer-${project.version}
sh ./deploySite.sh -Preporting

To deploy main version http://maven.apache.org/enforcer, use
sh ./deploySite.sh -Preporting -Psite-release

Note you can add arguments to the script to pass your svn credentials:
-Dusername=
-Dpassword=

Workflow for site when releasing
--------------------------------
Once release staged, you can publish a staged site.
cd target/checkout
sh ./deploySite.sh -Preporting
content will be in http://maven.apache.org/enforcer-archives/enforcer-${project.version}

Once vote passed, redeploy main site:
cd target/checkout (or use the version tag)
sh ./deploySite.sh -Preporting -Psite-release

