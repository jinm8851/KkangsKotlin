// MyAIDLInterface.aidl
package org.study2.ch15_outer;

// Declare any non-default types here with import statements
//외부앱과 연동목적으로 aidl파일만듬
//외부에서 뭔가를 호출하기 위한 함수를 나열
//aidl파일을 만들면 외부에 소스가 만들어지기 때문에 꼭 빌드에 메이크 모줄을 실행시켜줘야 앱에서 읽을수 있음
interface MyAIDLInterface {
    int getMaxDuration();
    void start();
    void stop();
}