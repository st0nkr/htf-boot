package com.teto;

import com.teto.command.Context;
import com.teto.domain.file.FileExtension;
import com.teto.domain.meta.Tag;
import com.teto.domain.provenance.Provenance;
import com.teto.domain.script.Script;
import com.teto.domain.script.ScriptCategory;

public interface IGoBuster extends IProperties {
    default Script goBusterS3(Context ctx) {
        Script s = new Script();
        s.setProxyChains(true);
        s.setName(Provenance.GoBusterS3.name());
        s.setExecutable("gobuster");
        String file = property(ctx, Tag.S3BucketNamesWordList);
        String cmd = "s3 -d $domain -w "+file+" --no-color --follow-redirect -t 4 --delay 1s -o $txt";
        s.setOutputFormat(FileExtension.json.name());
        s.setCommandLine(s.getExecutable()+" "+cmd);
        s.setMainCategory(ScriptCategory.DIRECTORIES.name());
        return s;
    }
    default Script goBusterDNS(Context ctx) {
        Script s = new Script();
        s.setProxyChains(true);
        s.setName(Provenance.GoBusterDNS.name());
        s.setExecutable("gobuster");
        String cmd = "dns --domain $domain -w $wordList(dnsmap.txt) --no-color -t 4 --delay 1s -o $txt";
        s.setOutputFormat(FileExtension.json.name());
        s.setCommandLine(s.getExecutable()+" "+cmd);
        s.setMainCategory(ScriptCategory.DIRECTORIES.name());
        return s;
    }

    default Script goBusterTFtp(Context ctx) {
        Script s = new Script();
        s.setProxyChains(true);
        s.setName(Provenance.GoBusterTFtp.name());
        String file = property(ctx, Tag.TFTPWordList);
        s.setExecutable("gobuster");
        String cmd = "tftp --server $ip -w "+file+" --no-color --follow-redirect --delay 1s -o $txt";
        s.setOutputFormat(FileExtension.json.name());
        s.setCommandLine(s.getExecutable()+" "+cmd);
        s.setMainCategory(ScriptCategory.DIRECTORIES.name());
        return s;
    }
    default Script goBusterVHost(Context ctx) {
        Script s = new Script();
        s.setProxyChains(true);
        s.setName(Provenance.GoBusterVHosts.name());
        s.setExecutable("gobuster");
        String vhosts = property(ctx, Tag.VirtualHostsWordList);
        String cmd = "vhost -u $url -w "+vhosts+" --no-color -useragent $userAgent -t 4 --delay 1s -o $txt";
        s.setOutputFormat(FileExtension.json.name());
        s.setCommandLine(s.getExecutable()+" "+cmd);
        s.setMainCategory(ScriptCategory.DIRECTORIES.name());
        return s;
    }
    default Script goBusterDir(Context ctx) {
        Script s = new Script();
        s.setProxyChains(true);
        s.setName(Provenance.GoBusterDir.name());
        s.setExecutable("gobuster");
        String cmd = "dir -u $url -w $secList(Discovery/Web-Content/big.txt) --status-codes-blacklist \"\" --no-color -s $statusCodesOfInterest --follow-redirect -useragent $userAgent -t 4 --delay 1s -o $txt";
        s.setOutputFormat(FileExtension.json.name());
        s.setCommandLine(s.getExecutable()+" "+cmd);
        s.setMainCategory(ScriptCategory.DIRECTORIES.name());
        return s;
    }
}
