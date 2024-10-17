package main

import "fmt"

func main() {
	b := map[interface{}]int{}
	var s Some
	b[s] = 3
	fmt.Println(b[nil])

}

type Some interface {
	methods()
}
