# KAICO
KAIST Cryptocurrency Mock Investment Application
업비트 API를 이용한 가상화폐 모의 투자 어플리케이션

## 코인만이 살길입니다.
<img width="100%" src="https://user-images.githubusercontent.com/96763888/148904692-af7d43ec-db94-4d57-85d7-4b8b98f3f981.gif" />
<p align="center"><em>지금, 독수리는 고고하게 시장을 관망하고 있다.</em></p>
<p align="center"><em>At the moment, the eagle is obediently watching the market.</em></p>
 
<img src="https://user-images.githubusercontent.com/49427080/148929638-7c02ee2b-f2fd-4669-8b65-d3f77b76eeea.gif" width="200" height="400"/>
> Lottie를 이용한 Splash 화면


## 로그인

<img src = "https://user-images.githubusercontent.com/49427080/148937916-f8bc2f03-31e3-4067-8d4e-3051f8f2353b.gif" width="200" height="400"/> <img src = "https://user-images.githubusercontent.com/49427080/148938462-dc4a9638-3f13-4ead-a88e-99361bf9987c.gif" width="200" height="400"/>

아이디와 비밀번호를 입력하여 로그인 할 수 있습니다. 해당 아이디가 DB에 존재하지 않거나 비밀번호가 다르면 로그인이 되지 않습니다.  
회원 가입을 통해 새로 유저를 등록할 수 있습니다. 중복된 아이디 사용은 불가하며 회원가입 성공 시 자동으로 로그인됩니다.

* 회원 가입 시 최초 제공 시드머니는 '5억 원' 입니다.

카카오 로그인 API를 이용하여 카카오 계정으로 회원가입 할 수 있습니다. 한 번 회원가입 시 카카오 id가 DB에 저장되어 다음 접근부터는 로그인으로 처리됩니다.  
뒤로 가기 버튼을 누르면, 어플리케이션이 종료되게 만들었습니다.

## Tab1 - 코인목록

먼저, 간단한 DB 유저 스키마 소개를 하겠습니다.  
본 어플리케이션은 단 하나의 스키마를 사용하였고, 각각의 정보는 아이디, 비밀번호, 현재 현금 보유량, 가상화폐 보유 목록(array)로 구성됩니다.

``` Kotlin
const UserSchema = new mongoose.Schema({
    name: String,
    pass: String,
    account: Number,
    trk: [{
        coinName: String,
        trade: Number,
        amount: Number,
    }],
    saveDate: {
        type: Date,
        defualt: Date.now,
    },
})

``` 
UpBit API 를 사용하여 가상화폐의 실시간 시세를 불러와 보여주는 탭입니다. 타이머 기능을 이용하여 1초 간격으로 업데이트 되도록 설정되었습니다.  
초당 API 요청 제한이 있어 제공되는 가상화폐의 개수는 거래량 상위, 혹은 제가 실제로 거래했던 종류로 10개만 제공됩니다.  
검색 기능을 통해 원하는 가상화폐의 정보를 찾을 수 있습니다. 티커, 한글 이름 모두 가능합니다.  

가상화폐 목록을 클릭하면 매수, 매도를 할 수 있습니다. 소수점 단위 구매도 가능합니다.  
각각의 매수, 매도 시 User DB의 가상화폐 보유 리스트와 현재 현금 보유량이 변경되는 로직을 구현하였습니다.

<img src = "https://user-images.githubusercontent.com/49427080/148934111-83d7cbee-f07d-4609-a2c0-1f3e91d87a9b.gif" width="200" height="400"/> <img src = "https://user-images.githubusercontent.com/49427080/148934881-d9777c40-3fe9-424a-8f4e-690c45b3d02b.gif" width="200" height="400"/>



## Tab2 - 투자내역

현재 유저의 가상화폐 보유량, 현금 보유량을 DB에서 가져와 총 자산 현황 및 가상화폐 종류별 자산 현황을 확인하는 곳입니다.  
실시간 가격변동을 반영하여 2초 간격으로 실시간 업데이트됩니다.  
현재 현금 보유량, 총 자산 평가, 총 매수액, 매수 자산의 현재 평가금액, 수익률 및 수익금 등을 확인할 수 있으며 이 정보는 각각의 가상화폐별로 모두 확인할 수 있습니다.  
보유하지 않은 가상화폐는 나타나지 않도록 수정했습니다.  


<img src = "https://user-images.githubusercontent.com/49427080/148935194-6b1ad42b-8406-4002-9f36-787f2fb23bf9.gif" width="200" height="400"/> 


## Tab3 - 랭킹

<img src = "https://user-images.githubusercontent.com/49427080/148935676-3e7dacba-05bf-4d54-8c6c-7f1911464f2d.gif" width="200" height="400"/> <

DB에서 모든 유저의 가상화폐 보유 목록을 가져와 Tab1의 실시간 가격과 연동하여 모든 유저의 실시간 자산 평가가 가능합니다.  
이 탭에서는 모든 유저의 총 자산을 비교하여 순위를 매깁니다. 가상화폐의 가격이 실시간으로 변하기 때문에 순위 또한 2초 간격으로 실시간 업데이트 됩니다.  
1등, 2등, 3등은 금, 은, 동 색깔로 표현하여 자부심을 가질 수 있도록 했습니다. 1등을 쟁취하여 투자의 귀재 가 되어보세요.

## Tab4 - 사용자 정보

<img src = "https://user-images.githubusercontent.com/49427080/148936480-ec6c6912-c1d3-4038-b0c9-ba8ad7f02876.gif" width="200" height="400"/> <img src = "https://user-images.githubusercontent.com/49427080/148936923-5c5625a4-6dce-4a8b-a44d-bdf77e52d80d.gif" width="200" height="400"/> <img src = "https://user-images.githubusercontent.com/49427080/148937207-33967df5-c773-466b-90bf-f2c7935c8bd7.gif" width="200" height="400"/> <img src = "https://user-images.githubusercontent.com/49427080/148937586-de85b631-f2e0-4c38-bbfa-4a37557c7736.gif" width="200" height="400"/>

* 회원탈퇴 - DB에 저장된 유저 정보가 삭제되어 앱을 탈퇴할 수 있습니다. 로그인 화면으로 되돌아갑니다.
* 비밀번호 변경 - 저장된 비밀번호를 변경할 수 있습니다. DB에서 비밀번호 변경
* 로그아웃 - 앱 로그아웃을 할 수 있습니다. 로그인 화면으로 되돌아갑니다.
* 앱 초기화 - 저장된 보유 자산을 초기화합니다(5억 원). 보유한 모든 가상화폐가 같이 초기화됩니다. 


## CREDITS
박승민 : smpak@kaist.ac.kr
김민정 : whitedaisy12759@gmail.com

### Backend info : Node.js, Socket.io, MongoDB
참고하세요 : https://github.com/smpak19/Stock_backend

