Agent7
======
An open source penetration testing tool

Wiki: https://github.com/DynisDev/Agent7/wiki  


**Overview**
 --------
Agent7 was created in the interest of helping others. This open source, free penetration testing tool is designed to create a simple, yet secure environment for testing your code, while implementing safeguards to stop you from attacking others. If you are here to hack, leave. If you are here to test the integrity of your website, ftp server, or even just a normal password, go no further. Agent7 will NEVER release any information about vulnerabilities and passwords. Agent7 runs on a basic premise. Simply click a button to begin a test. When you click this button, Agent7 will prompt you for extra information, enter in the info, and press go. The test will then begin, and depending on the test type and parameters, it may take a while for it to finish.  


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
        - Rewrite GUI ☑   
        - Re-implement URL discovery mode with php fuzzing and remove unknown  
        blue screen of death bug created when url discovery mode is enabled. ☐   
        - Fix reflected xss detection ☐   
   Lower Priority:  
   --------------  
        - Fix intelligent fuzzer or remove it (preferably the first) ☐   
   Aesthetic(I'll implement these last, but they can be added at any time):
   -------------- 
        - Add themes ☐   
        - Add graph for the url fuzzer ☐   
        
        

**License:**
  ----
  Copyright(c) November 1st, 2013 Dylan T. Katz
  
  For the newest license, see the LICENSE.txt in this github repo.
