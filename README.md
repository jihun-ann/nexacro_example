# nexacro_example

nexacro 예제
UX/UI툴로 주로 사용
일반적인 함수, 메서드 작성과는 조금 차이가 있음 

//obj : 이벤트가 일어나는 객체, 해당 객체의 속성 예를들어 obj:nexacro.Button, e : 이벤트가 일어나는 객체 결과적으로 이벤트가 일어나는 객체
this.함수명 = function(obj : i, e : i){
  ~~기능~
}

호출시에도 this.함수명(); 으로 호출

동작하는 함수가 아닌 임의의 함수를 만들경우에는 obj, e 생략
