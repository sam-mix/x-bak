package main

import "fmt"

func main() {
	// a := map[int]int{1: 1}
	// for k := range a {
	// 	fmt.Println(&a[k]) // 无法寻址，这个数据属于临时的，可变的数据
	// }

	b := []int{1, 2, 3}

	for k := range b {

		// 这个可以寻址，每个切片值都会持有一个底层数组，
		// 而这个底层数组中的每个元素值都是有一个确切的内存地址
		fmt.Println(&b[k])
	}
}
