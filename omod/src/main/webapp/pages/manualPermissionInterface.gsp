
${ ui.includeFragment("simpleformservice", "header") }
        
<script>
    function submit(){
        simpleformservice.simple_permission_manager.give_permission({
            granted_to_person_uuid : document.getElementById("granted_to_person_uuid").value,
            encounter_type : document.getElementById("encounter_type").value,
            permission_type : document.getElementById("permission_type").value,
        })
    }
</script>

<div>
    <div style = 'font-size:36px;'>
        SimpleFormService Manual Permission Interface
    </div>
    <br>
    <div style = 'font-size:13px;'>
        intended to be used for testing operations - theres a reason its not pretty
    </div>
</div>
<div style = 'height:50px;'></div>
<div>
    <div style='font-size:21px;'>
        Create permission
    </div>
    <Br>
    <div style = 'display:flex;'>
        <div style = 'width:400px;'>Granted To Person UUID</div>
        <input placeholder = "" id = 'granted_to_person_uuid'>
    </div>
    <div style = 'display:flex;'>
        <div style = 'width:400px;'>Encounter Type</div>
        <input placeholder = "" id = 'encounter_type'>
    </div>
    <div style = 'display:flex;'>
        <div style = 'width:400px;'>Permission Type (choose `create`, `read`, or `delete`)</div>
        <input placeholder = "" id = 'permission_type'>
    </div>
    <br>
    <button onclick = 'submit()'> create permission</button>
</div>

<!-- test data -->
<!-- 

    d99d2110-7cad-4282-9e00-ede06b92c965

-->

