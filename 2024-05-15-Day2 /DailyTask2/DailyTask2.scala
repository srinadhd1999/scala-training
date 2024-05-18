import scala.io.StdIn.*

def sortingAlgorithms(algoName: String): (Array[Int]) => Unit = {
    println(algoName)
    algoName match {
        case "bubble" => (arr) => {
            for(i <- 0 until arr.length-1) {
                for(j <- 0 until arr.length-i-1) {
                    if(arr(j)>arr(j+1)) {
                        var p=arr(j)
                        arr(j)=arr(j+1)
                        arr(j+1)=p
                    }
                }
            }
            for(i <- 0 until arr.length) {
                var t=arr(i)
                print(s"$t ")
            }
            println()
        }

        case "binary_search" => (arr) => {
            var low=0
            var high=arr.length-1
            println("Enter the element to be searched")
            val target = readInt()
            var flag = false
            while(low<=high) {
                val mid = low + (high -low)/2
                if(arr(mid)==target) {
                    flag = true
                    print(s"$target Found at index $mid")
                } 
                else if(arr(mid)>target) {
                    high=mid-1
                }
                else {
                    low=mid+1    
                }
                if(flag == true) {
                    low=1
                    high=0
                }
            }
            if(flag == false) {
                println("Not found")
            }
            println()
        }

        case "insertion" => (arr) => {
            for (i <- 1 until arr.length) {
                val key = arr(i)
                var j = i - 1

                while (j >= 0 && arr(j) > key) {
                    arr(j + 1) = arr(j)
                    j -= 1
                }
                arr(j + 1) = key
            }
            for(i <- 0 until arr.length) {
                var t=arr(i)
                print(s"$t ")
            }
            println()
        }

        case "quick" => (arr) => {
            quicksort(arr, 0, arr.length-1)
            for(i <- 0 until arr.length) {
                var t=arr(i)
                print(s"$t ")
            }
            println()
        }

        case "merge" => (arr) => {
            val result = mergesort(arr, 0, arr.length-1)
            for(i <- 0 until result.length) {
                var t=result(i)
                print(s"$t ")
            }
            println()
        }

        case "radix" => (arr) => {
            radixSort(arr)
            for(i <- 0 until arr.length) {
                var t=arr(i)
                print(s"$t ")
            }
            println()
        }
    }
}

//--------------------------quicksort------------------------------
def swap(a: Array[Int], pos1: Int, pos2: Int): Unit = {
    val stash = a(pos1)
    a(pos1) = a(pos2)
    a(pos2) = stash
  }

def quicksort(a: Array[Int], low: Int, hi: Int): Unit = {
    if (low < hi) {
      val p = partition(a, low, hi)
      quicksort(a, low, p-1)
      quicksort(a, p+1, hi)
    }
}

def partition(subArray: Array[Int], low: Int, hi: Int): Int = {
    val pivot = hi;
    var i = low;
    for (
      j <- low to hi
      if subArray(j) < subArray(pivot)
    ) {swap(subArray, i, j); i+=1}
    swap(subArray, i, pivot);
    return i
}
//-----------------------------------------------------------------

//-------------------------mergesort-------------------------------
def mergesort(a: Array[Int], lo: Int, hi: Int) : Array[Int] = {
    if (hi - lo < 1){
      return a.slice(lo, hi+1)
    }
    val mid = (hi - lo)/2 + lo
    val left = mergesort(a, lo, mid)
    val right = mergesort(a, mid+1, hi)
    return merge(left, right)
  }

//mergesort
def merge(a: Array[Int], b: Array[Int]): Array[Int] = {
    var result = Array.fill(a.length + b.length)(0)
    var i: Int = 0
    var j: Int = 0
    while (i < a.length || j < b.length){
      if (i >= a.length){
        result(i+j) = b(j)
        j+=1
      }
      else if (j >= b.length || a(i) <= b(j)) {
        result(i+j) = a(i)
        i+=1
      }
      else {
        result(i+j) = b(j)
        j+=1
      }
    }
    return result
  }

//-------------------------------------------------------------------

//-----------------------------radix sort----------------------------

def getMax(arr: Array[Int]): Int = {
    return arr.max
  }

  def countSort(arr: Array[Int], exp: Int): Unit = {
    val n = arr.length
    val output = new Array[Int](n)
    val count = Array.fill(10)(0)

    for (i <- arr.indices) {
      count((arr(i) / exp) % 10) += 1
    }

    for (i <- 1 until 10) {
      count(i) += count(i - 1)
    }

    for (i <- arr.indices.reverse) {
      val digit = (arr(i) / exp) % 10
      output(count(digit) - 1) = arr(i)
      count(digit) -= 1
    }

    for (i <- arr.indices) {
      arr(i) = output(i)
    }
  }

  def radixSort(arr: Array[Int]): Array[Int] = {
    val m = getMax(arr)
    var exp = 1
    while (m / exp > 0) {
      countSort(arr, exp)
      exp *= 10
    }
    return arr
  }

//-------------------------------------------------------------------

@main def main() = {
    println("Enter the size of the array")
    val n = readInt()
    val arr: Array[Int] = new Array[Int](n)
    println("Enter the numbers")
    for(i <- 0 until arr.length) {
        arr(i) = readInt()
    }

    val result1 = sortingAlgorithms("bubble")(arr)
    val result2 = sortingAlgorithms("binary_search")(arr)
    val result3 = sortingAlgorithms("insertion")(arr)
    val result4 = sortingAlgorithms("quick")(arr)
    val result5 = sortingAlgorithms("merge")(arr)
    val result6 = sortingAlgorithms("radix")(arr)
}