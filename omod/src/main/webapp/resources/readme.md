## `simple_form_submission_handler` needs to be light but extendable. 
To this end, we use the `answer_handlers` directory to build "answer handlers".


- these `answer_handlers` handle retreiving answers from `<simpleanswer>...</simpleanswer>` containers of the type the answer extension is defiend for     
    - e.g., `answer_handler['boolean']` only handles answers of the type `<simpleanswer type = "">`
    
In this way, it is kept light by not loading all extensions unnessesarily, but still enables defining as many handlers (i.e., extensions) as desired