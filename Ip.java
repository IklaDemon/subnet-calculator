public class Ip
{
    private int otteti[];
    private int maschera_cidr;
    private long ip;
    private long netmask;

    public Ip(String text)
    {
        otteti = new int[4];
        int index = 0;
        String num = "";
        ip = 0x00;
        netmask = 0x00;

        for(int i = 0; i < 4; i++)
        {
            while(text.charAt(index) != '.' && text.charAt(index) != '/')
            {
                num = num + text.charAt(index);
                index++;
            }
            index++;
            otteti[i] = Integer.parseInt(num);
            num = "";
        }
        num = "";
        while(index < text.length())
        {
            num = num + text.charAt(index);
            index++;
        }
        maschera_cidr = Integer.parseInt(num);

        int cnt = maschera_cidr;

        for(int i = 0; i < 32; i++)
        {
            if(cnt > 0)
            {
                netmask = netmask | 0x1;
            }
            netmask = netmask << 1;
            cnt--;
        }
        netmask = netmask >> 1;

        for(int i = 0; i < 4; i++)
        {
            ip = ip | otteti[i];
            ip = ip << 8;
        }
        ip = ip >> 8;
    }

    public String getClasse()
    {
        if(((otteti[0] >> 7) & 0x01) == 0x00) return "A";
        if(((otteti[0] >> 6) & 0x03) == 0x02) return "B";
        if(((otteti[0] >> 5) & 0x07) == 0x06) return "C";
        if(((otteti[0] >> 4) & 0x0f) == 0x0e) return "D";
        if(((otteti[0] >> 4) & 0x0f) == 0x0f) return "E";
        return "errore";
    }

    public String getType()
    {
        if(otteti[0] == 10) return "private";
        if(otteti[0] == 172 && otteti[1] >= 16 && otteti[1] <= 31) return "private";
        if(otteti[0] == 192 && otteti[1] == 168) return "private";
        return "public";
    }

    public String getIpAddress()
    {
        String str;
        str = otteti[0] + "." + otteti[1] + "." + otteti[2] + "." + otteti[3] + " (";

        for(int i = 0; i < 4; i++)
        {
            for(int j = 7; j >= 0; j--)
            {
                str = str + ((otteti[i] >> j) & 0x01);
            }
            if(i < 3) str = str + ".";
        }
        return str + ")";
    }

    public String getCidrNotation()
    {
        return "/" + maschera_cidr;
    }

    public String getMask()
    {
        String str_netmask = "";
        String str_netmask_bin = "";
        for(int i = 31; i >= 0; i--)
        {
            str_netmask_bin += (netmask >> i) & 0x01;
            if(i == 8 || i == 16 || i == 24) str_netmask_bin += ".";
        }
        for(int i = 0; i < 32; i += 8)
        {
            if(i == 8 || i == 16 || i == 24) str_netmask = "." + str_netmask;
            str_netmask = ((netmask >> i) & 0xff) + str_netmask;
        }
        return str_netmask + " (" + str_netmask_bin + ")";
    }

    public String getWildcardMask()
    {
        long wildcardmask = ~ netmask;
        String str_wild = "";
        String str_wild_bin = "";
        for(int i = 31; i >= 0; i--)
        {
            str_wild_bin += (wildcardmask >> i) & 0x01;
            if(i == 8 || i == 16 || i == 24) str_wild_bin += ".";
        }
        for(int i = 0; i < 32; i += 8)
        {
            if(i == 8 || i == 16 || i == 24) str_wild = "." + str_wild;
            str_wild = ((wildcardmask >> i) & 0xff) + str_wild;
        }
        return str_wild + " (" + str_wild_bin + ")";
    }

    public String getNetworkAddress()
    {
        long network_ip = ip & netmask;
        String str_network_ip = "";
        for(int i = 0; i < 32; i += 8)
        {
            if(i == 8 || i == 16 || i == 24) str_network_ip = "." + str_network_ip;
            str_network_ip = ((network_ip >> i) & 0xff) + str_network_ip;
        }   
        return str_network_ip;
    }

    public String getUsableHostRange()
    {
        long network_ip = ip & netmask;
        long network_ip_start = network_ip | 0x01;
        long network_ip_end = ip | (~ netmask);
        if(maschera_cidr != 32) network_ip_end = network_ip_end & 0xfffffffe;
        String str_network_ip_start = "";
        String str_network_ip_end = "";

        for(int i = 0; i < 32; i += 8)
        {
            if(i == 8 || i == 16 || i == 24) str_network_ip_start = "." + str_network_ip_start;
            str_network_ip_start = ((network_ip_start >> i) & 0xff) + str_network_ip_start;
        }

        for(int i = 0; i < 32; i += 8)
        {
            if(i == 8 || i == 16 || i == 24) str_network_ip_end = "." + str_network_ip_end;
            str_network_ip_end = ((network_ip_end >> i) & 0xff) + str_network_ip_end;
        }

        return str_network_ip_start + " -> " + str_network_ip_end;
    }

    public String getBroadcast()
    {
        long network_broadcast = ip | (~ netmask);
        String str_network_broadcast = "";

        for(int i = 0; i < 32; i += 8)
        {
            if(i == 8 || i == 16 || i == 24) str_network_broadcast = "." + str_network_broadcast;
            str_network_broadcast = ((network_broadcast >> i) & 0xff) + str_network_broadcast;
        }

        return str_network_broadcast;
    }

    public int getNumberHosts()
    {
        int esponente = 0;

        for(int i = 0; i < 32; i++)
        {
            esponente += (((~ netmask) >> i) & 0x01);
        }

        return (int) (Math.pow(2, esponente) - 2);
    }
}
