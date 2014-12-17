__author__ = 'wuyanzhe'

import time
import dns.name
import dns.message
import dns.query
import dns.flags

class record:
    def __init__(self):
        ISOTIMEFORMAT='%Y-%m-%d %X'
        record_time = time.strftime(ISOTIMEFORMAT,time.localtime())
        file_in = open("record/"+record_time+".txt","a")
        list_ = []
        file = open('website.txt')
        fileList = file.readlines()
        for fileLine in fileList:
            line = str(fileLine)
            web = line.split(",")
            webdomain = web[1].strip('\n')  
            print(webdomain)
            domain = webdomain
            name_server = '8.8.4.4'
            ADDITIONAL_RDCLASS = 65535
            domain = dns.name.from_text(domain)
            if not domain.is_absolute():
                domain = domain.concatenate(dns.name.root)
            request = dns.message.make_query(domain, dns.rdatatype.ANY)
            request.find_rrset(request.additional, dns.name.root, ADDITIONAL_RDCLASS,
                   dns.rdatatype.OPT, create=True, force_unique=True)
            response_udp = dns.query.udp(request, name_server)
            list_.append(response_udp)
            file_in.write(str(response_udp))
            file_in.write("\n")
            file_in.write("\n")
        file.close()
        print(list_)
        file_in.close()



R = record()