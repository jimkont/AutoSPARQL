// TO BE
// ------
//	是 么 || (S DP[subject] (VP V:'是' DP[object]) PP:'么')  || <x, l1, t, [ l1:[ | ], l2:[ | x=y ] ], [ (l3,x,subject,<<e,t>,t>), (l4,y,object,<<e,t>,t>) ], [  l3<l1, l4<l1, l2<scope(l3), l2<scope(l4) ],[]>
//	是  || (S DP[subject] (VP V:'是' ADJ[comp]))   || <x, l1, t, [ l1:[ | x=y ]], [ (l2,x,subject,<<e,t>,t>), (l3,y,comp,<e,t>) ], [  l2=l1, l3=l2 ],[]>

    是 什么 || (S DP[subject] (VP V:'是' N:'什么')) || <x, l1, t, [ l1:[  | ] ], [ (l2,x,subject,<<e,t>,t>) ], [ l2=l1 ],[]>
    是  || (S DP[subject] (VP V:'是' DP[object]))  || <x, l1, t, [ l1:[ | ], l2:[ | x=y ] ], [ (l3,x,subject,<<e,t>,t>), (l4,y,object,<<e,t>,t>) ], [  l3<l1, l4<l1, l2<scope(l3), l2<scope(l4) ],[]>
    有  || (S DP[subject] (VP V:'有' DP[object]))  || <x, l1, t, [ l1:[ | ], l2:[ | x=y ] ], [ (l3,x,subject,<<e,t>,t>), (l4,y,object,<<e,t>,t>) ], [  l3<l1, l4<l1, l2<scope(l3), l2<scope(l4) ],[]>

    谁      || (DP N:'谁')       || <x, l1, <<e,t>,t>, [ l1:[ ?x | ] ], [], [], []>

    有 哪些 || (S DP[subject] (VP V:'有' N:'哪些')) || <x, l1, t, [ l1:[  | ] ], [ (l2,x,subject,<<e,t>,t>) ], [ l2=l1 ],[]>
    在 哪里 || (S DP[subject] (VP V:'在' N:'哪里')) || <x, l1, t, [ l1:[  | ] ], [ (l2,x,subject,<<e,t>,t>) ], [ l2=l1 ],[]>
    是 多少 || (S DP[subject] (VP V:'是' N:'多少')) || <x, l1, t, [ l1:[  | ] ], [ (l2,x,subject,<<e,t>,t>) ], [ l2=l1 ],[]>

    在 哪 || (S DP[subject] (VP V:'在' N:'哪')) || <x, l1, t, [ l1:[  | ] ], [ (l2,x,subject,<<e,t>,t>) ], [ l2=l1 ],[]>
    是 多少 || (S DP[subject] (VP V:'是' N:'多少')) || <x, l1, t, [ l1:[  | ] ], [ (l2,x,subject,<<e,t>,t>) ], [ l2=l1 ],[]>
    是 谁 || (S DP[subject] (VP V:'是' N:'谁')) || <x, l1, t, [ l1:[  | ] ], [ (l2,x,subject,<<e,t>,t>) ], [ l2=l1 ],[]>

	//是否有  || (S V:'是否' C:'有' DP[dp])  || <x, l1, t, [ l1:[ | ] ], [ (l2,x,dp,<<e,t>,t>) ], [ l2=l1 ],[]>
	//是否存在  || (S V:'是否' C:'存在' DP[dp])  || <x, l1, t, [ l1:[ | ] ], [ (l2,x,dp,<<e,t>,t>) ], [ l2=l1 ],[]>

//    有 多少 || (S (VP V:'有' (DP N:'多少') DP[object]))]) || <y, l1, t, [ l1:[ | l2:[ y | ] HOWMANY y l3:[|] ] ], [ (l4,y,object,<<e,t>,t>) ], [ l4=l2 ],[  SLOT_arg/PROPERTY/y ]>
//    有 多少 || (DP WH:'how' ADJ:'many' NP[noun]) || <y, l1, <<e,t>,t>, [ l1:[ | l2:[ y | ] HOWMANY y l3:[|] ] ], [ (l4,y,noun,<e,t>) ], [ l4=l2 ],[  SLOT_arg/RESOURCE/y ]>
//	有 多少 || (S (VP V:'有' N:'多少') NP[noun]) || <y, l1, <<e,t>,t>, [ l1:[ | l2:[ y | ] HOWMANY y l3:[|] ] ], [ (l4,y,noun,<e,t>) ], [ l4=l2 ],[]>
//    有 多少 || (S (VP V:'有' N:'多少') NP[noun]) || <y, l1, <<e,t>,t>, [ l1:[ | l2:[ y | ] HOWMANY y l3:[|] ] ], [ (l4,y,noun,<e,t>) ], [ l4=l2 ],[]>
//    有 多少  || (VP V:'how' DET:'many')  || <x,l1,e, [ l1:[ | count(x) ] ], [],[],[]>
//    有 多少 || (S DP[subject] (VP V:'有' N:'多少')) || <x, l1, t, [ l1:[ | l2:[ x | ] HOWMANY y l3:[|] ] ], [ (l4,x,subject,<<e,t>,t>) ], [ l4=l2 ],[]>
    //有 多少 || (S DP[dp] (VP V:'有' N:'多少')) || <x, l1, t, [ l1:[ | ]  ], [ (l2,x,dp,<<e,t>,t>) ], [ l2=l1 ],[]>

    多少 || (DP N:'多少' DP[dp]) || <y, l1, <<e,t>,t>, [ l1:[ | l2:[ y | ] HOWMANY y l3:[|] ] ], [ (l4,y,dp,<<e,t>,t>) ], [ l4=l2 ],[  SLOT_arg/RESOURCE/y ]>
    有 || (S V:'有' DP[dp]) || <x, l1, t, [ l1:[ | ] ], [ (l2,x,dp,<<e,t>,t>) ], [ l2=l1 ],[]>

// TO BE: YES/NO QUESTIONS

//	是 || (S (VP V:'是' DP[subject] DP[object]))   || <x, l1, t, [ l1:[ | ], l2:[ | x=y ] ], [ (l3,x,subject,<<e,t>,t>), (l4,y,object,<<e,t>,t>) ], [  l3<l1, l4<l1, l2<scope(l3), l2<scope(l4) ],[]>
	
//	是 || (S (VP V:'是' DP[subject] ADJ[comp]))    || <x, l1, t, [ l1:[ | x=y ]], [ (l2,x,subject,<<e,t>,t>), (l3,y,comp,<e,t>) ], [  l2=l1, l3=l2 ],[]>
	
// IMPERATIVES
// ---------------------

//	给我 || (S (VP V:'给' (DP N:'我') DP[object])) || <x,l1,t,[ l1:[ ?x | x=y ] ],[ (l2,y,object,<<e,t>,t>) ],[ l1=l2 ],[]>
//	说出 || (S (VP V:'说出' DP[object])) || <x,l1,t,[ l1:[ ?x | x=y ] ],[ (l2,y,object,<<e,t>,t>) ],[ l1=l2 ],[]>
//	列举 || (S (VP V:'列举' DP[object])) || <x,l1,t,[ l1:[ ?x | x=y ] ],[ (l2,y,object,<<e,t>,t>) ],[ l1=l2 ],[]>
	列出 所有 || (S (VP V:'列出' DET:'所有' DP[object])) || <x,l1,t,[ l1:[ ?x | x=y ] ],[ (l2,y,object,<<e,t>,t>) ],[ l1=l2 ],[]>
	所有 || (DP DET:'所有' NP[noun]) || <y, l1, <<e,t>,t>, [ l1:[ | l2:[ y | ] EVERY y l3:[|] ] ], [ (l4,y,noun,<e,t>) ], [ l4=l2 ],[]>

// DETERMINER
// ----------

//	一个  || (DP DET:'一个' NP[noun]) || <y, l1, <<e,t>,t>, [ l1:[ | l2:[ y | ] SOME y l3:[|] ] ], [ (l4,y,noun,<e,t>) ], [ l4=l2 ],[]>
//	每个 || (DP DET:'每个' NP[noun]) || <y, l1, <<e,t>,t>, [ l1:[ | l2:[ y | ] EVERY y l3:[|] ] ], [ (l4,y,noun,<e,t>) ], [ l4=l2 ],[]>
//	每一个 || (DP DET:'每一个' NP[noun]) || <y, l1, <<e,t>,t>, [ l1:[ | l2:[ y | ] EVERY y l3:[|] ] ], [ (l4,y,noun,<e,t>) ], [ l4=l2 ],[]>
//	最多 || (DP ADJ:'最多' NP[noun]) || <y, l1, <<e,t>,t>, [ l1:[ | l2:[ y | ] THEMOST y l3:[|] ] ], [ (l4,y,noun,<e,t>) ], [ l4=l2 ],[]>
//	最大的 || (DP ADJ:'最大的' NP[noun]) || <y, l1, <<e,t>,t>, [ l1:[ | l2:[ y | ] THEMOST y l3:[|] ] ], [ (l4,y,noun,<e,t>) ], [ l4=l2 ],[]>
	最大的 || (DP ADJ:'最大的' NP[noun]) || <y, l1, <<e,t>,t>, [ l1:[ | l2:[ y | ] THEMOST y l3:[|] ] ], [ (l4,y,noun,<e,t>) ], [ l4=l2 ],[]>
    最 || (DP RB:'最' ADJ* NP[noun]) || <y, l1, <<e,t>,t>, [ l1:[ | l2:[ y | ] THEMOST y l3:[|] ] ], [ (l4,y,noun,<e,t>) ], [ l4=l2 ],[]>

//	最少 || (DP ADJ:'最少' NP[noun]) || <y, l1, <<e,t>,t>, [ l1:[ | l2:[ y | ] THELEAST y l3:[|] ] ], [ (l4,y,noun,<e,t>) ], [ l4=l2 ],[]>
//	一些 || (DP ADJ:'一些' NP[noun]) || <y, l1, <<e,t>,t>, [ l1:[ | l2:[ y | ] AFEW y l3:[|] ] ], [ (l4,y,noun,<e,t>) ], [ l4=l2 ],[]>
//	某些 || (DP DET:'某些' NP[noun]) || <x, l1, <<e,t>,t>, [ l1:[ x | ] ], [ (l2,x,noun,<e,t>) ], [ l2=l1 ],[]>
//	哪些 || (DP DET:'哪些' NP[noun]) || <y, l1, <<e,t>,t>, [ l1:[ ?y | ] ], [ (l2,y,noun,<e,t>) ], [ l2=l1 ],[]>
//	什么 || (DP DET:'什么' NP[noun]) || <y, l1, <<e,t>,t>, [ l1:[ ?y | ] ], [ (l2,y,noun,<e,t>) ], [ l2=l1 ],[]>
//	是什么 || (NP VP V:'是' NP[noun]) || <y, l1, <<e,t>,t>, [ l1:[ ?y | ] ], [ (l2,y,noun,<e,t>) ], [ l2=l1 ],[]>
//	多少 || (DP DET:'多少' NP[noun]) || <y, l1, <<e,t>,t>, [ l1:[ | l2:[ y | ] MANY y l3:[|] ] ], [ (l4,y,noun,<e,t>) ], [ l4=l2 ],[]>
//	至少 || (DP DET:'至少' NUM[num] NP[noun]) || <y,l1,<<e,t>,t>,[l1:[ y | count_greatereq(y,x) ]],[(l2,y,noun,<e,t>),(l3,x,num,e)],[ l1=l2, l2=l3 ],[]>
//	至多 || (DP DET:'至多' NUM[num] NP[noun]) || <y,l1,<<e,t>,t>,[l1:[ y | count_lesseq(y,x) ]],[(l2,y,noun,<e,t>),(l3,x,num,e)],[ l1=l2, l2=l3 ],[]>
//	正好 || (DP DET:'正好' NUM[num] NP[noun]) || <y,l1,<<e,t>,t>,[l1:[ y | count_eq(y,x) ]],[(l2,y,noun,<e,t>),(l3,x,num,e)],[ l1=l2, l2=l3 ],[]>


//	其他 || (NP ADJ:'其他' NP*) || <x,l1,<e,t>,[ l1:[ | ] ], [],[],[]>
//	总共 || (NP[ ADJ:'总共' NP[np]) || <s,l1,<e,t>,[ l1:[ ?s | sum(a,x,s) ] ], [ (l2,x,np,<e,t>) ],[ l2=l1 ],[]>

//	最少 || (ADJ DET:'最少' ADJ*) || <x,l1,<e,t>,[ l1:[ | minimum(a,x,x) ] ], [],[],[]>

//  	有多少  || (DET DET:'有多少')  || <x,l1,e, [ l1:[ ?x | ] ], [],[],[]>
//  	多少次  || (DP DET:'多少次') || <x,l1,<<e,t>,t>, [ l1:[ | count(x) ] ], [],[],[]>
//	几次 || (DP DET:'几次') || <x,l1,<<e,t>,t>, [ l1:[ | count(x) ] ], [],[],[]>
//	一个  || (DET DET:'一个') || <x,l1,e, [ l1:[ x |] ], [],[],[]>
//	哪个 || (DET DET:'哪个') || <x,l1,e, [ l1:[ ?x |] ], [],[],[]>
//	最多 || (DET DET:'最多') || <y, l1, e, [ l1:[ | l2:[ y | ] THEMOST y l3:[|] ] ], [], [],[]>
//	最少 || (DET DET:'最少') || <y, l1, e, [ l1:[ | l2:[ y | ] THELEAST y l3:[|] ] ], [], [],[]>

    // NECESSARY "CHEAT"
	最高 || (NP ADJ:'最高' NP*) || <x, l1, e, [ l1:[ j | SLOT_high(x,j), maximum(j) ] ],[],[],[ SLOT_high/PROPERTY/height ]> ;; <x, l1, e, [ l1:[ | maximum(x) ] ], [], [],[]>

	// COUNT
//	多于 || (DP DET:'多于' NUM[num] NP[np]) || <y,l1,<<e,t>,t>,[ l1:[ y,c | count_greater(y,z) ] ],[(l2,y,np,<e,t>),(l3,z,num,e)],[l2=l1,l3=l1],[]> ;; <y,l1,<<e,t>,t>,[ l1:[ y | greater(y,z) ] ],[(l2,y,np,<e,t>),(l3,z,num,e)],[l2=l1,l3=l1],[]>
//    以上 || (DP DET:'以上' NUM[num] NP[np]) || <y,l1,<<e,t>,t>,[ l1:[ y,c | count_greater(y,z) ] ],[(l2,y,np,<e,t>),(l3,z,num,e)],[l2=l1,l3=l1],[]> ;; <y,l1,<<e,t>,t>,[ l1:[ y | greater(y,z) ] ],[(l2,y,np,<e,t>),(l3,z,num,e)],[l2=l1,l3=l1],[]>
//	超过 || (DP DET:'超过' NUM[num] NP[np]) || <y,l1,<<e,t>,t>,[ l1:[ y,c | count_greater(y,z) ] ],[(l2,y,np,<e,t>),(l3,z,num,e)],[l2=l1,l3=l1],[]> ;; <y,l1,<<e,t>,t>,[ l1:[ y | greater(y,z) ] ],[(l2,y,np,<e,t>),(l3,z,num,e)],[l2=l1,l3=l1],[]>
	
//	少于 || (DP DET:'少于' NUM[num] NP[np]) || <y,l1,<<e,t>,t>,[ l1:[ y,c | count_less(y,z) ] ],[(l2,y,np,<e,t>),(l3,z,num,e)],[l2=l1,l3=l1],[]> ;; <y,l1,<<e,t>,t>,[ l1:[ y | less(y,z) ] ],[(l2,y,np,<e,t>),(l3,z,num,e)],[l2=l1,l3=l1],[]>
   
	// HOW
	// how || (DP DET:'how' ADJ[adj]) || <x,l1,<<e,t>,t>,[ l1:[?x,|] ],[ (x,l2,adj,<e,t>) ],[l2=l1],[]>


// EMPTY STUFF
// ------------

//	也 || (VP ADV:'也' VP*) || <x,l1,t,[ l1:[|] ],[],[],[]>
//	也 || (DP ADV:'也' DP*) || <x,l1,<<e,t>,t>,[ l1:[|] ],[],[],[]>

//	with || (NP NP* (PP P:'with' DP[dp])) || <x,l1,<e,t>,[ l1:[| empty(x,y) ] ],[(l2,y,dp,<<e,t>,t>)],[l2=l1],[]>
//      of   || (NP NP* (PP P:'of' DP[dp])) || <x,l1,<e,t>,[ l1:[| empty(x,y) ] ],[(l2,y,dp,<<e,t>,t>)],[l2=l1],[]>

//	人 || (NP N:'人') || <x,l1,<e,t>,[ l1:[|] ],[],[],[]>
//    还 || (ADJ ADJ:'还' ADJ*) || <x,l1,<e,t>,[l1:[|]],[],[],[]>


// WH WORDS
// --------

//	什么    || (DP WH:'什么')      || <x, l1, <<e,t>,t>, [ l1:[ ?x | ] ], [], [], []>
//	哪些    || (DP WH:'哪些')     || <x, l1, <<e,t>,t>, [ l1:[ ?x | ] ], [], [], []>
//	哪个    || (DP WH:'哪个')     || <x, l1, <<e,t>,t>, [ l1:[ ?x | ] ], [], [], []>


//	多少 || (DP ADJ:'多少' NP[noun]) || <y, l1, <<e,t>,t>, [ l1:[ | l2:[ y | ] HOWMANY y l3:[|] ] ], [ (l4,y,noun,<e,t>) ], [ l4=l2 ],[]>
//   总和　|| (DP WH:'总和　' NP[noun]) || <y, l1, <<e,t>,t>, [ l1:[ ?y | ] ], [ (l4,y,noun,<e,t>) ], [ l4=l1 ],[]>
//	谁      || (DP WH:'谁')       || <x, l1, <<e,t>,t>, [ l1:[ ?x | ] ], [], [], []>
//	什么时候     || (S WH:'什么时候' S[s])  || <x, l1, t, [ l1:[ ?x | SLOT_p(y,x) ] ], [(l2,y,s,t)], [l2=l1], [ SLOT_p/PROPERTY/date ]>
//	什么时间     || (S WH:'什么时间' S[s])  || <x, l1, t, [ l1:[ ?x | SLOT_p(y,x) ] ], [(l2,y,s,t)], [l2=l1], [ SLOT_p/PROPERTY/date ]>
//	哪年        || (S WH:'哪年' S[s])  || <x, l1, t, [ l1:[ ?x | SLOT_p(y,x) ] ], [(l2,y,s,t)], [l2=l1], [ SLOT_p/PROPERTY/date ]>
	
//	什么时候    || (DP WH:'什么时候')      || <y, l1, <<e,t>,t>, [ l1:[ ?x | SLOT_p(y,x) ] ], [], [], [ SLOT_p/PROPERTY/date ]>
//	什么时间    || (DP WH:'什么时间')      || <y, l1, <<e,t>,t>, [ l1:[ ?x | SLOT_p(y,x) ] ], [], [], [ SLOT_p/PROPERTY/date ]>
//	哪年    || (DP WH:'哪年')      || <y, l1, <<e,t>,t>, [ l1:[ ?x | SLOT_p(y,x) ] ], [], [], [ SLOT_p/PROPERTY/date ]>
//	哪    || (S WH:'哪' S[s]) || <x, l1, t, [ l1:[ ?x | SLOT_p(y,x) ] ], [(l2,y,s,t)], [l2=l1], [ SLOT_p/PROPERTY/place ]>
//	哪里    || (S WH:'哪里' S[s]) || <x, l1, t, [ l1:[ ?x | SLOT_p(y,x) ] ], [(l2,y,s,t)], [l2=l1], [ SLOT_p/PROPERTY/place ]>
	
//	哪    || (DP WH:'哪')     || <y, l1, <<e,t>,t>, [ l1:[ ?x | SLOT_p(y,x) ] ], [], [], [ SLOT_p/PROPERTY/place ]>
//	哪里    || (DP WH:'哪里')     || <y, l1, <<e,t>,t>, [ l1:[ ?x | SLOT_p(y,x) ] ], [], [], [ SLOT_p/PROPERTY/place ]>
	
//	在哪里 || (DP WH:'哪里' (PP P:'在' DP[dp])) || <y, l1, <<e,t>,t>, [ l1:[ ?x | SLOT_p(y,x), SLOT_in(x,z) ] ], [(l2,z,dp,<<e,t>,t>)], [l2=l1], [ SLOT_p/PROPERTY/place ]>

// NEGATION
// --------

//   不 || (ADJ NEG:'不' ADJ*) || <x,l2,t,[ l1:[ | NOT l2:[|] ] ],[],[],[]>
//   不 || (VP NEG:'不' VP*) || <x,l2,t,[ l1:[ | NOT l2:[|] ] ],[],[],[]>


// COORDINATION
// ------------

//	和 || (S S* CC:'和' S[s]) || <x,l1,t,[l1:[|]],[(l2,y,s,t)],[l1=l2],[]>
//	和 || (DP DP* CC:'和' DP[dp]) || <x,l1,<<e,t>,t>,[l1:[|]],[(l2,y,dp,<<e,t>,t>)],[l1=l2],[]>
//	和 || (NP NP* CC:'和' NP[np]) || <x,l1,<e,t>,[l1:[|x=y]],[(l2,y,np,<e,t>)],[l1=l2],[]>
//	和 || (VP VP* CC:'和' VP[vp]) || -
//	和 || (ADJ ADJ* CC:'和' ADJ[adj]) || -

//	以及 || (NP NP* CC:'以及' NP[np]) || <x,l1,<e,t>,[l1:[|]],[(l2,y,np,<e,t>)],[l1=l2],[]>

//	或 || (S S* CC:'或' S[2]) || -
//	或 || (DP DP* CC:'或' DP[2]) || -
//	或 || (NP NP* CC:'或' NP[2]) || -
//	或 || (VP VP* CC:'或' VP[2]) || -
//	或 || (ADJ ADJ* CC:'或' ADJ[2]) || -


// EXISTENTIAL
// -----------

//	there || (DP (NP EX:'there')) || <x,l1,<<e,t>,t>,[l1:[|]],[],[],[]>
// NUMBERS (1-10)
// ---------------------

//	一   || (NP NUM:'一' NP*)   || <x,l1,<e,t>,[l1:[x|count(x,1)]],[],[],[]>
//	二   || (NP NUM:'二' NP*)   || <x,l1,<e,t>,[l1:[x|count(x,2)]],[],[],[]>
//	三   || (NP NUM:'三' NP*)   || <x,l1,<e,t>,[l1:[x|count(x,3)]],[],[],[]>
//	四   || (NP NUM:'四' NP*)   || <x,l1,<e,t>,[l1:[x|count(x,4)]],[],[],[]>
//	五   || (NP NUM:'五' NP*)   || <x,l1,<e,t>,[l1:[x|count(x,5)]],[],[],[]>
//	六   || (NP NUM:'六' NP*)   || <x,l1,<e,t>,[l1:[x|count(x,6)]],[],[],[]>
//	七   || (NP NUM:'七' NP*)   || <x,l1,<e,t>,[l1:[x|count(x,7)]],[],[],[]>
//	八   || (NP NUM:'八' NP*)   || <x,l1,<e,t>,[l1:[x|count(x,8)]],[],[],[]>
//	九   || (NP NUM:'九' NP*)   || <x,l1,<e,t>,[l1:[x|count(x,9)]],[],[],[]>
//	十   || (NP NUM:'十' NP*)   || <x,l1,<e,t>,[l1:[x|count(x,10)]],[],[],[]>

//	一   || (NP NUM:'一' NP*)   || <x,l1,<e,t>,[l1:[x|equal(x,1)]],[],[],[]>
//	二   || (NP NUM:'二' NP*)   || <x,l1,<e,t>,[l1:[x|equal(x,2)]],[],[],[]>
//	三   || (NP NUM:'三' NP*)   || <x,l1,<e,t>,[l1:[x|equal(x,3)]],[],[],[]>
//	四   || (NP NUM:'四' NP*)   || <x,l1,<e,t>,[l1:[x|equal(x,4)]],[],[],[]>
//	五   || (NP NUM:'五' NP*)   || <x,l1,<e,t>,[l1:[x|equal(x,5)]],[],[],[]>
//	六   || (NP NUM:'六' NP*)   || <x,l1,<e,t>,[l1:[x|equal(x,6)]],[],[],[]>
//	七   || (NP NUM:'七' NP*)   || <x,l1,<e,t>,[l1:[x|equal(x,7)]],[],[],[]>
//	八   || (NP NUM:'八' NP*)   || <x,l1,<e,t>,[l1:[x|equal(x,8)]],[],[],[]>
//	九   || (NP NUM:'九' NP*)   || <x,l1,<e,t>,[l1:[x|equal(x,9)]],[],[],[]>
//	十   || (NP NUM:'十' NP*)   || <x,l1,<e,t>,[l1:[x|equal(x,10)]],[],[],[]>