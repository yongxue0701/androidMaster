
function onClick_Submit_user_interface() {

    var ansQ1 = "empty";
    var ansQ2 = "empty";
    var ansQ3 = "empty";
    var ansQ4 = "empty";

    var counts = 0;

    var ansQ1NL = document.getElementsByName("q1");
    var ansQ2NL = document.getElementsByName("q2");
    var ansQ3NL = document.getElementsByName("q3");
    var ansQ4NL = document.getElementsByName("q4");

    for(var i = 0; i < ansQ1NL.length; i++) {
        if(ansQ1NL[i].checked) {
            ansQ1 = ansQ1NL[i].value;
            counts = counts + 1;
        }

        if(ansQ2NL[i].checked) {
            ansQ2 = ansQ2NL[i].value;
            counts = counts + 1;
        }

        if(ansQ3NL[i].checked) {
            ansQ3 = ansQ3NL[i].value;
            counts = counts + 1;
        }

        if(ansQ4NL[i].checked) {
            ansQ4 = ansQ4NL[i].value;
            counts = counts + 1;
        }
    }

    var answers = [ansQ1,ansQ2,ansQ3,ansQ4];
    UserInterfaceQuiz.getQuizAnsFromWebView(answers,counts);
}

function displayResult_user_interface() {

    var answer = FragmentQuizResults.returnQuizAnsToWebView();
    var answers = answer.split(",");

    var correctAns = ["c","c","b","b"];

    var para_q1 = document.getElementById("q1_explain_p");
    var para_q2 = document.getElementById("q2_explain_p");
    var para_q3 = document.getElementById("q3_explain_p");
    var para_q4 = document.getElementById("q4_explain_p");

    var img_q1 = document.getElementById("q1_explain_img");
    var img_q2 = document.getElementById("q2_explain_img");
    var img_q3 = document.getElementById("q3_explain_img");
    var img_q4 = document.getElementById("q4_explain_img");

    var points = 0;
    for(var j = 0;j < correctAns.length;j++) {
        if(answers[j] == correctAns[j]) {
            points = points + 1;
        }
    }

    if (points == 1 || points == 0) {
        document.getElementById("quiz_results").innerHTML = "Your Score: " + points + " point";
    } else if (points > 1) {
        document.getElementById("quiz_results").innerHTML = "Your Score: " + points + " points";
    }

    for(var i = 1;i <= correctAns.length; i++) {
        document.getElementById("q"+i+"a").disabled = true;
        document.getElementById("q"+i+"b").disabled = true;
        document.getElementById("q"+i+"c").disabled = true;
        document.getElementById("q"+i+"d").disabled = true;
    }

    if(answers[0] != "empty") {
        document.getElementById("q1"+answers[0]).checked = true;
        if(answers[0] == correctAns[0]) {
            para_q1.innerHTML = "You are correct!";
            img_q1.src = "file:///android_res/drawable/correct_ans.png";
        } else {
            para_q1.innerHTML = "Fragments are re-usable components and can be used in multiple activities.";
            img_q1.src = "file:///android_res/drawable/wrong_ans.png";
        }
    } else {
        para_q1.innerHTML = "You haven't answered this question!";
        img_q1.src = "file:///android_res/drawable/wrong_ans.png";
    }

    if(answers[1] != "empty") {
        document.getElementById("q2"+answers[1]).checked = true;
        if(answers[1] == correctAns[1]) {
            para_q2.innerHTML = "You are correct!";
            img_q2.src = "file:///android_res/drawable/correct_ans.png";
        } else {
            para_q2.innerHTML = "Fragments life cycle will be affected by activity's life cycle. ";
            img_q2.src = "file:///android_res/drawable/wrong_ans.png";
        }
    } else {
        para_q2.innerHTML = "You haven't answered this question!";
        img_q2.src = "file:///android_res/drawable/wrong_ans.png";
    }

    if(answers[2] != "empty") {
        document.getElementById("q3"+answers[2]).checked = true;
        if(answers[2] == correctAns[2]) {
            para_q3.innerHTML = "You are correct!";
            img_q3.src = "file:///android_res/drawable/correct_ans.png";
        } else {
            para_q3.innerHTML = "Fragment generally will contribute its UI by using"
            +" its own layout. But it might not have one in some cases.";
            img_q3.src = "file:///android_res/drawable/wrong_ans.png";
        }
    } else {
        para_q3.innerHTML = "You haven't answered this question!";
        img_q3.src = "file:///android_res/drawable/wrong_ans.png";
    }

    if(answers[3] != "empty") {
        document.getElementById("q4"+answers[3]).checked = true;
        if(answers[3] == correctAns[3]) {
            para_q4.innerHTML = "You are correct!";
            img_q4.src = "file:///android_res/drawable/correct_ans.png";
        } else {
            para_q4.innerHTML = "We have to implement onCreateView() to return "+
            "the layout of fragment as part of fragments UI contribution."
            img_q4.src = "file:///android_res/drawable/wrong_ans.png";
        }
    } else {
        para_q4.innerHTML = "You haven't answered this question!";
        img_q4.src = "file:///android_res/drawable/wrong_ans.png";
    }
}