const fs=require("fs"),path=require("path");
const out=process.argv[2]||"docs/design/piku-pack";
const G=["..11......11....","..1111111111....","..1111111111....",".111111111111...",".111111111111...",".111111111111.1.",".111111111111111",".11111111111.111","..1111111111.11.","..1111111111.11.","..11......11...."];
const R=G.length,C=G[0].length,fill=new Set(),outline=new Set();
G.forEach((row,r)=>[...row].forEach((ch,c)=>{if(ch==="1")fill.add(r+","+c)}));
for(const k of fill){const[r,c]=k.split(",").map(Number);for(let dr=-1;dr<=1;dr++)for(let dc=-1;dc<=1;dc++){const n=(r+dr)+","+(c+dc);if(!fill.has(n))outline.add(n)}}
const GREEN="#81EE77",PINK="#FF5C8A",K="#000000";
const P=a=>a.map(x=>x.join(","));
const S={
idle:[K,P([[3,4],[4,4],[3,9],[4,9]])],
blink:[K,P([[4,4],[4,5],[4,9],[4,10]])],
dizzy:[K,P([[3,3],[3,5],[4,4],[5,3],[5,5],[3,8],[3,10],[4,9],[5,8],[5,10]])],
love:[PINK,P([[3,3],[3,5],[4,3],[4,4],[4,5],[5,4],[3,8],[3,10],[4,8],[4,9],[4,10],[5,9]])],
sleep:[K,P([[4,3],[4,4],[4,5],[4,8],[4,9],[4,10]])],
happy:[K,P([[4,3],[3,4],[4,5],[4,8],[3,9],[4,10]])]};
const W=C+2,H=R+2;
fs.mkdirSync(path.join(out,"states"),{recursive:true});
const xml=st=>{const[oc,ps]=S[st],over=new Map(ps.map(p=>[p,oc])),by=new Map();
 const add=(k,col)=>{const[r,c]=k.split(",").map(Number);if(!by.has(col))by.set(col,[]);by.get(col).push([r,c])};
 for(const k of outline)add(k,K);for(const k of fill)add(k,over.get(k)||GREEN);
 const segs=a=>{const rows=new Map();for(const[r,c]of a){if(!rows.has(r))rows.set(r,[]);rows.get(r).push(c)}
  const out=[];for(const[r,cs]of rows){cs.sort((x,y)=>x-y);let s=cs[0],p=cs[0]
   for(let i=1;i<=cs.length;i++){if(i<cs.length&&cs[i]===p+1){p=cs[i];continue}
    const len=p-s+1;out.push(`M${s+1},${r+1}h${len}v1h-${len}z`);if(i<cs.length){s=cs[i];p=cs[i]}}}
  return out.join("")};
 const paths=[...by].map(([col,a])=>`  <path android:fillColor="${col}" android:pathData="${segs(a)}"/>`).join("\n");
 return `<vector xmlns:android="http://schemas.android.com/apk/res/android"\n  android:width="${W*8}dp" android:height="${H*8}dp" android:viewportWidth="${W}" android:viewportHeight="${H}">\n${paths}\n</vector>\n`};
for(const st of Object.keys(S))fs.writeFileSync(path.join(out,"states","piku_"+st+".xml"),xml(st));
fs.writeFileSync(path.join(out,"piku_vector.xml"),xml("idle"));
fs.writeFileSync(path.join(out,"ANIMATION_SPEC.md"),`# Piku animation spec
Sprites: piku_idle, piku_blink, piku_dizzy, piku_love, piku_sleep, piku_happy (VectorDrawable). Render crisp, no filtering/blur.
1. Idle: piku_idle with small up-down bob (about 3dp, 1.2s loop). Every 3-5 s a blink: piku_blink 120ms.
2. Draggable anywhere; on release a small settle bounce.
3. Edge dock: released within 24dp of left/right edge snaps there, half hidden, with a small chevron handle; tap handle to bring back.
4. Tap/long-press menu pill: Dance, Nap, Hide.
   Dance: piku_happy and piku_love alternate every 300ms, tilt left/right 8 degrees, bubble "Let's dance!" 2s.
   Nap: piku_sleep, slow bob, rising fading "Zzz"; tap to wake: piku_idle + bubble "Hey there!".
   Hide: dock to nearest edge.
5. Shake or fast drag: piku_dizzy, wobble, bubble "Woah.. dizzy..." 2s, then idle.
6. Bubble: white rounded pill, 1dp dark outline, small tail, plain text, auto-hide 2s.
7. Reactions: agent working = faster bob; task done = piku_happy; error = piku_dizzy; idle 60s = piku_sleep.
8. If system animation scale is 0: static piku_idle only.
9. Setting "Show mascot" (default on). Never cover the Stop button or the input box.
Rule: existing physics/drag/fling/menu code stays; only replace art and add what is missing.
`);
console.log("ok",out);
