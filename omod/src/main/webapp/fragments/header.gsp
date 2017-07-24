<head>
    <title>OpenMRS SimpleFormService API Interface </title>
    <link rel="shortcut icon" type="image/ico" href="/openmrs/images/openmrs-favicon.ico">
    <link rel="icon" type="image/png\" href="/openmrs/images/openmrs-favicon.png">
    
    
    <link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script type="text/javascript" src="${ ui.resourceLink("uicommons", "/scripts/jquery-ui-1.9.2.custom.min.js")}"></script>
    <script type="text/javascript" src="${ ui.resourceLink("uicommons", "/scripts/underscore-min.js")}"></script>
    <script type="text/javascript" src="${ ui.resourceLink("uicommons", "/scripts/jquery.toastmessage.js")}"></script>
    <script type="text/javascript" src="${ ui.resourceLink("uicommons", "/scripts/jquery.simplemodal.1.4.4.min.js")}"></script>
    
    <!-- initialize interface handlers -->
    <script type="text/javascript" src="${ ui.resourceLink("simpleformservice", "/initialize.js") }"></script>
    
    <!--  define resource_root globally --> 
    <script>
        if(typeof global === "undefined") var global = {};
        global.resource_root  = "/openmrs/ms/uiframework/resource/simpleformservice/";
    </script>
    
    <!-- rename jquery -->
    <script type="text/javascript">
        jq = jQuery;
    </script>
</head>
