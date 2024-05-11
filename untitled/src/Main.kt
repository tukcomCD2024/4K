fun main() {

    var r : Int = readLine()!!.toInt()
    for(i in 1..r){
        var a0 : Int = readLine()!!.toInt()
        var a1 = Array<Int>(a0) { readLine()!!.toInt()}
        var a2 = Array<Int>(a0) { readLine()!!.toInt()}
        var total : Int = 0;
        for(j in a1){
            total += j
        }
        for(h in a2){
            total += h
        }
        var max = 0
        var tmp : Int = 0
        var loop :Int = a0*2 -1
        for(s in 0..loop){
            for(s in 1..loop){}
            if(a1[s]==0)
                continue
            tmp += a1[s]
            a2[s] = 0
            if(s - 1 != -1)
                a1[s-1] = 0
            if(s + 1 != a0)
                a1[s+1] = 0
        }

        println(max)
    }
}