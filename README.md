Agent7 - Now abandoned :(
======
An open source penetration testing tool
**This project is now abandoned due to several reasons, firstly, there are a lot of better scanners, and I decided that my project was re-inventing the wheel. Secondly, I believe this project has many bugs, and upon looking back on it, it has the potential to damage webpages if not used properly. Anyone may still contribute, but know that I most likely won't be working on this project for a while, most likely indefinately. HOWEVER, you can send me a PM with any questions.**
Wiki: https://github.com/DynisDev/Agent7/wiki 

Creator: Dylan Katz  
Homescreen background and program theme: Kevyn Latham  

**Overview**
 --------
Agent7 was created in the interest of helping others. This open source, free penetration testing tool is designed to create a simple, yet secure environment for testing your code, while implementing safeguards to stop you from attacking others. If you're here to test the integrity of your website, ftp server, or even just a normal password, go no further. Agent7 will NEVER release any information about vulnerabilities and passwords. Agent7 runs on a basic premise. Simply click a button to begin a test. When you click this button, Agent7 will prompt you for extra information, enter in the info, and press go. The test will then begin, and depending on the test type and parameters, it may take a while for it to finish.  

**IMPORTANT**
  --------
  Agent7 is not intended for use without consent of the website owner and in some cases, the website host. On website builders such as weebly and webs, you would need consent from company hosting your site, and providing your web building service. If Agent7 is used for malicious purposes such as hacking/compromising databases, the responsibility is that of the end user. Developers assume no liability and are not responsible for misuse or damage caused when using this program for unintended purposes. This program is **ONLY** for use where you have consent from a person or persons authorized to give you consent to use it, and only where they give you consent to use it.
  
**Compiling**
 -----------
If you wish to compile the source of this program, you'll need the Apache Commons Net 3.3, Apache Commons Lang3 3.1, and Jsoup 1.7.2.
    
you can also use maven by navigating to this repository, and using 
    
    mvn clean install
    
    
For more info on how to use Agent7, go to https://github.com/DynisDev/Agent7/wiki/_pages  



**TODO**(feel free to help me in my tasks by making a pull request.):
 ------
   High priority:  
   -------------- 
        - Re-implement URL discovery mode with php fuzzing and remove unknown  
        blue screen of death bug created when url discovery mode is enabled. ☐   
        - Fix reflected xss detection ☐   
   Lower Priority:  
   --------------  
        - Rewrite GUI ☑   
        - Fix intelligent fuzzer or remove it (removed) ☑   
        (why do I have all of these done? lol, so much for priority...)
   Aesthetic(I'll implement these last, but they can be added at any time):
   -------------- 
        - Add themes ☐   
        - Add graph for the url fuzzer ☐   
        
        

**License:**
  ----
  Copyright(c) November 1st, 2013 Dylan T. Katz
  
  For the newest license, see the LICENSE.txt in this github repo.
